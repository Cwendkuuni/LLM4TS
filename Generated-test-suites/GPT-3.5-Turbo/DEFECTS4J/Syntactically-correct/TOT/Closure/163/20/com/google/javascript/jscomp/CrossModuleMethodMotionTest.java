import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Property;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Symbol;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TestExternsBuilder;
import com.google.javascript.jscomp.testing.TestUtils;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrossModuleMethodMotionTest {

    @Mock
    private AbstractCompiler compiler;
    @Mock
    private IdGenerator idGenerator;
    @Mock
    private AnalyzePrototypeProperties analyzer;
    @Mock
    private JSModuleGraph moduleGraph;

    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() {
        boolean canModifyExterns = true;
        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    }

    @Test
    public void process_ShouldCallAnalyzePrototypePropertiesAndMoveMethods() {
        Node externRoot = new Node(Token.EMPTY);
        Node root = new Node(Token.SCRIPT);
        Collection<NameInfo> allNameInfo = new ArrayList<>();

        Node parent = new Node(Token.SCRIPT);
        Node child = new Node(Token.GETPROP, new Node(Token.THIS),
                new Node(Token.STRING, "child"));
        root.addChildToFront(parent);
        root.getLastChild().addChildToFront(child);

        when(moduleGraph.getModuleCount()).thenReturn(2);
        when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);

        crossModuleMethodMotion.process(externRoot, root);

        Mockito.verify(analyzer).process(externRoot, root);
        Mockito.verify(analyzer).getAllNameInfo();
    }

    @Test
    public void process_ShouldNotMoveMethods_IfModuleCountIsLessThan2() {
        Node externRoot = new Node(Token.EMPTY);
        Node root = new Node(Token.SCRIPT);
        Collection<NameInfo> allNameInfo = new ArrayList<>();

        when(moduleGraph.getModuleCount()).thenReturn(1);
        when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);

        crossModuleMethodMotion.process(externRoot, root);

        Mockito.verify(moduleGraph).getModuleCount();
        Mockito.verify(analyzer).getAllNameInfo();
        Mockito.verifyZeroInteractions(analyzer);
    }

    @Test
    public void process_ShouldReportError_IfDeepestCommonModuleRefIsNull() {
        Node externRoot = new Node(Token.EMPTY);
        Node root = new Node(Token.SCRIPT);
        Collection<NameInfo> allNameInfo = new ArrayList<>();
        allNameInfo.add(new NameInfo());

        when(moduleGraph.getModuleCount()).thenReturn(2);
        when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);

        crossModuleMethodMotion.process(externRoot, root);

        Mockito.verify(compiler).report(any(JSError.class));
    }

    @Test
    public void moveMethods_ShouldSkipUnreferencedNameInfos() {
        Collection<NameInfo> allNameInfo = new ArrayList<>();
        allNameInfo.add(new NameInfo());

        crossModuleMethodMotion.moveMethods(allNameInfo);

        Mockito.verifyZeroInteractions(compiler);
    }

    @Test
    public void moveMethods_ShouldSkipIfNameInfoReadsClosureVariables() {
        Collection<NameInfo> allNameInfo = new ArrayList<>();
        NameInfo nameInfo = new NameInfo();
        nameInfo.setReadsClosureVariables(true);
        allNameInfo.add(nameInfo);

        crossModuleMethodMotion.moveMethods(allNameInfo);

        Mockito.verifyZeroInteractions(compiler);
    }

    @Test
    public void moveMethods_ShouldSkipIfSymbolIsNotProperty() {
        Collection<NameInfo> allNameInfo = new ArrayList<>();
        NameInfo nameInfo = new NameInfo();
        nameInfo.setDeepestCommonModuleRef(new JSModule("test"));
        Symbol symbol = new Symbol();
        allNameInfo.add(nameInfo);

        crossModuleMethodMotion.moveMethods(allNameInfo);

        Mockito.verifyZeroInteractions(compiler);
    }

    @Test
    public void moveMethods_ShouldStubAndUnstubMethod() {
        Collection<NameInfo> allNameInfo = new ArrayList<>();

        // Create nodes required for the test
        Node name = new Node(Token.NAME);
        name.setString("foo");

        Node value = new Node(Token.SCRIPT);
        Node valueParent = new Node(Token.NAME, new Node(Token.GETPROP), name.cloneNode().detachFromParent());
        value.addChildToFront(valueParent);

        Node valueParentParent = new Node(Token.NAME, new Node(Token.STRING));
        valueParentParent.addChildToBack(name.cloneNode());

        Node node1 = new Node(Token.SCRIPT, value.cloneNode(), valueParentParent);

        Node objLit = new Node(Token.OBJECTLIT);
        Node prop = new Node(Token.STRING, name.cloneNode().detachFromParent());
        objLit.addChildToFront(prop);
        Node objLitParent = new Node(Token.OBJECTLIT);
        objLitParent.addChildToFront(objLit);
        Node node2 = new Node(Token.SCRIPT, objLitParent, name.cloneNode().detachFromParent());

        Node declExp = new Node(Token.EXPR_RESULT,
                new Node(Token.ASSIGN, new Node(Token.STRING, name.cloneNode().detachFromParent()), node1));
        Node declExpParent = new Node(Token.SCRIPT);
        declExpParent.addChildToFront(declExp.getParent());
        Node node3 = new Node(Token.SCRIPT, declExpParent, objLit.cloneNode().detachFromParent());

        NameInfo nameInfo = new NameInfo();

        JSModule deepestCommonModuleRef = new JSModule("test");
        nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);
        allNameInfo.add(nameInfo);

        Node compilerNode = new Node(Token.NAME);
        compilerNode.setString("Move methods");
        reset(compiler);
        when(idGenerator.newId()).thenReturn(1);
        when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);
        when(compiler.parseSyntheticCode(any(String.class))).thenReturn(compilerNode);
        when(compiler.getNodeForCodeInsertion(deepestCommonModuleRef)).thenReturn(objLit.cloneNode());

        crossModuleMethodMotion.moveMethods(allNameInfo);

        Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    }

    @Test
    public void moveMethods_ShouldDeclareStubFunctionsInTopMostModule() {
        Collection<NameInfo> allNameInfo = new ArrayList<>();
        NameInfo nameInfo = new NameInfo();
        JSModule deepestCommonModuleRef = new JSModule("test");
        nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);
        allNameInfo.add(nameInfo);

        Node compilerNode = new Node(Token.NAME);
        compilerNode.setString("Move methods");
        reset(compiler);
        when(idGenerator.newId()).thenReturn(1);
        when(idGenerator.hasGeneratedAnyIds()).thenReturn(true);
        when(compiler.parseSyntheticCode(any(String.class))).thenReturn(compilerNode);
        when(compiler.getNodeForCodeInsertion(null)).thenReturn(compilerNode);

        crossModuleMethodMotion.moveMethods(allNameInfo);

        Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    }
}