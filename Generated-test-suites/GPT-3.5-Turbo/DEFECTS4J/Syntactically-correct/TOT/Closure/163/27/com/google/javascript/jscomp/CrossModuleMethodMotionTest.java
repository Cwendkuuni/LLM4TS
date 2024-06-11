
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private AbstractCompiler compiler;
    private JSModuleGraph moduleGraph;
    private AnalyzePrototypeProperties analyzer;
    private CrossModuleMethodMotion motion;

    @Before
    public void setUp() {
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        compiler = mock(AbstractCompiler.class);
        moduleGraph = mock(JSModuleGraph.class);
        analyzer = mock(AnalyzePrototypeProperties.class);
        motion = new CrossModuleMethodMotion(compiler, idGenerator, false);
        motion.analyzer = analyzer;
        motion.moduleGraph = moduleGraph;
    }

    @Test
    public void testProcess_onlyOneModule() {
        Node externRoot = IR.script();
        Node root = IR.script();

        when(moduleGraph.getModuleCount()).thenReturn(1);

        motion.process(externRoot, root);

        verify(analyzer, never()).process(any(), any());
    }

    @Test
    public void testProcess_nullCommonModuleRef() {
        Node externRoot = IR.script();
        Node root = IR.script();

        when(moduleGraph.getModuleCount()).thenReturn(2);
        when(analyzer.getAllNameInfo()).thenReturn(Collections.singletonList(createNameInfo(null)));

        motion.process(externRoot, root);

        verify(compiler).report(any(JSError.class));
    }

    @Test
    public void testProcess_moveMethods() {
        Node externRoot = IR.script();
        Node root = IR.script();
        JSModule module1 = createModule("module1");
        JSModule module2 = createModule("module2");
        JSModule module3 = createModule("module3");

        when(moduleGraph.getModuleCount()).thenReturn(3);
        when(moduleGraph.dependsOn(module1, module2)).thenReturn(true);
        when(moduleGraph.dependsOn(module1, module3)).thenReturn(false);
        when(analyzer.getAllNameInfo()).thenReturn(Collections.singletonList(createNameInfo(module1)));

        motion.process(externRoot, root);

        verify(compiler).reportCodeChange();
    }

    @Test
    public void testProcess_skipUnreferencedNameInfo() {
        Node externRoot = IR.script();
        Node root = IR.script();
        JSModule module1 = createModule("module1");

        when(moduleGraph.getModuleCount()).thenReturn(2);
        when(analyzer.getAllNameInfo()).thenReturn(Collections.singletonList(createUnreferencedNameInfo(module1)));

        motion.process(externRoot, root);

        verify(compiler, never()).report(any(JSError.class));
    }

    @Test
    public void testProcess_skipNameInfoWithClosureVariables() {
        Node externRoot = IR.script();
        Node root = IR.script();
        JSModule module1 = createModule("module1");

        when(moduleGraph.getModuleCount()).thenReturn(2);
        when(analyzer.getAllNameInfo()).thenReturn(Collections.singletonList(createClosureVariableNameInfo(module1)));

        motion.process(externRoot, root);

        verify(compiler, never()).report(any(JSError.class));
    }

    @Test
    public void testMoveMethods() {
        CrossModuleMethodMotion.NameInfo nameInfo = createNameInfo(mock(JSModule.class));
        nameInfo.setDeclarations(Arrays.asList(createProperty(), createProperty()));

        when(analyzer.getAllNameInfo()).thenReturn(Collections.singletonList(nameInfo));
        when(compiler.getNodeForCodeInsertion(any())).thenReturn(IR.empty());

        motion.moveMethods(analyzer.getAllNameInfo());

        verify(compiler, times(2)).reportCodeChange();
    }

    @Test
    public void testMoveMethods_stubGenerator() {
        CrossModuleMethodMotion.NameInfo nameInfo = createNameInfo(mock(JSModule.class));
        nameInfo.setDeclarations(Arrays.asList(createProperty(), createProperty()));

        when(analyzer.getAllNameInfo()).thenReturn(Collections.singletonList(nameInfo));
        when(compiler.getNodeForCodeInsertion(null)).thenReturn(IR.empty());

        motion.moveMethods(analyzer.getAllNameInfo());

        verify(compiler, times(2)).reportCodeChange();
    }

    private CrossModuleMethodMotion.NameInfo createNameInfo(JSModule module) {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        nameInfo.setDeepestCommonModuleRef(module);
        nameInfo.setReferenced(true);
        return nameInfo;
    }

    private CrossModuleMethodMotion.NameInfo createUnreferencedNameInfo(JSModule module) {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        nameInfo.setDeepestCommonModuleRef(module);
        nameInfo.setReferenced(false);
        return nameInfo;
    }

    private CrossModuleMethodMotion.NameInfo createClosureVariableNameInfo(JSModule module) {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        nameInfo.setDeepestCommonModuleRef(module);
        nameInfo.setReferenced(true);
        nameInfo.setReadsClosureVariables(true);
        return nameInfo;
    }

    private CrossModuleMethodMotion.Property createProperty() {
        Node value = IR.function(IR.block(IR.empty())).copyInformationFrom(IR.empty());
        Node declaration = IR.name("A.prototype.b");
        declaration.addChildToFront(value);
        Node prototype = IR.name("prototype");
        prototype.addChildToFront(IR.name("A"));
        return new CrossModuleMethodMotion.Property(declaration, prototype);
    }

    private JSModule createModule(String name) {
        JSModule module = mock(JSModule.class);
        when(module.toString()).thenReturn(name);
        return module;
    }
}
