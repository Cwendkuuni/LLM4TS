
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.CrossModuleMethodMotion;

public class CrossModuleMethodMotionTest {

    @Test
    public void testProcess() {
        // Test if process() correctly moves prototype methods deeper in the module graph
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGenerator, false);

        // create a mock Analyzer object
        AnalyzePrototypeProperties analyzePrototypeProperties = mock(AnalyzePrototypeProperties.class);
        Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = new ArrayList<>();

        // create a mock NameInfo object
        AnalyzePrototypeProperties.NameInfo nameInfo = mock(AnalyzePrototypeProperties.NameInfo.class);
        AnalyzePrototypeProperties.Symbol symbol = mock(AnalyzePrototypeProperties.Symbol.class);
        when(nameInfo.isReferenced()).thenReturn(true);
        when(nameInfo.getDeepestCommonModuleRef()).thenReturn(mock(JSModule.class));
        Iterator<AnalyzePrototypeProperties.Symbol> declarations = mock(Iterator.class);
        when(nameInfo.getDeclarations()).thenReturn(declarations);
        when(declarations.hasNext()).thenReturn(true, false);
        when(declarations.next()).thenReturn(symbol);
        when(symbol instanceof Property).thenReturn(true);

        // create a mock Property node
        Node value = mock(Node.class);
        when(symbol.getValue()).thenReturn(value);
        when(value.isFunction()).thenReturn(true);
        Node valueParent = mock(Node.class);
        when(value.getParent()).thenReturn(valueParent);
        when(valueParent.isGetterDef()).thenReturn(false);
        when(valueParent.isSetterDef()).thenReturn(false);
        Node proto = mock(Node.class);
        when(symbol.getPrototype()).thenReturn(proto);

        // mock method stubbing
        Node stubCall = mock(Node.class);
        Node unstubParent = mock(Node.class);
        Node unstubCall = mock(Node.class);

        // Stub method calls
        when(analyzePrototypeProperties.getAllNameInfo()).thenReturn(allNameInfo);
        when(idGenerator.newId()).thenReturn(1);
        when(declarations.hasNext()).thenReturn(true, false);
        when(declarations.next()).thenReturn(symbol);
        when(moduleGraph.dependsOn(any(JSModule.class), any(JSModule.class))).thenReturn(true);
        when(compiler.getNodeForCodeInsertion(any(JSModule.class))).thenReturn(unstubParent);
        when(IR.call(any(Node.class), any(Node.class))).thenReturn(stubCall);
        when(IR.call(any(Node.class), any(Node.class), any(Node.class))).thenReturn(unstubCall);
        when(IR.exprResult(any(Node.class))).thenReturn(mock(Node.class));
        when(IR.assign(any(Node.class), any(Node.class))).thenReturn(mock(Node.class));

        crossModuleMethodMotion.process(null, null);

        // Assert that the necessary methods were called
        verify(analyzePrototypeProperties).process(any(Node.class), any(Node.class));
        verify(compiler).reportCodeChange();
    }

    @Test
    public void testMoveMethods() {
        // Test if moveMethods() correctly moves prototype methods to a deeper module
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGenerator, false);

        // create mock NameInfo objects
        AnalyzePrototypeProperties.NameInfo nameInfo1 = mock(AnalyzePrototypeProperties.NameInfo.class);
        AnalyzePrototypeProperties.NameInfo nameInfo2 = mock(AnalyzePrototypeProperties.NameInfo.class);
        Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = new ArrayList<>();
        allNameInfo.add(nameInfo1);
        allNameInfo.add(nameInfo2);

        // create mock symbols
        AnalyzePrototypeProperties.Symbol symbol1 = mock(AnalyzePrototypeProperties.Symbol.class);
        AnalyzePrototypeProperties.Symbol symbol2 = mock(AnalyzePrototypeProperties.Symbol.class);

        // create a mock Property node
        Node value1 = mock(Node.class);
        Node value2 = mock(Node.class);
        Node symbolParent1 = mock(Node.class);
        Node symbolParent2 = mock(Node.class);
        Node symbolProto1 = mock(Node.class);
        Node symbolProto2 = mock(Node.class);

        // Stub method calls
        when(nameInfo1.isReferenced()).thenReturn(true);
        when(nameInfo1.getDeepestCommonModuleRef()).thenReturn(mock(JSModule.class));
        when(nameInfo1.getDeclarations()).thenReturn(mock(Iterator.class));
        when(nameInfo1.getDeclarations().hasNext()).thenReturn(true, false);
        when(nameInfo1.getDeclarations().next()).thenReturn(symbol1, symbol2);
        when(symbol1 instanceof Property).thenReturn(true);
        when(symbol1.getValue()).thenReturn(value1);
        when(value1.isFunction()).thenReturn(true);
        when(value1.getParent()).thenReturn(symbolParent1);
        when(symbolParent1.isGetterDef()).thenReturn(false);
        when(symbolParent1.isSetterDef()).thenReturn(false);
        when(symbol1.getPrototype()).thenReturn(symbolProto1);
        when(moduleGraph.dependsOn(any(JSModule.class), any(JSModule.class))).thenReturn(true);

        when(nameInfo2.isReferenced()).thenReturn(true);
        when(nameInfo2.getDeepestCommonModuleRef()).thenReturn(mock(JSModule.class));
        when(nameInfo2.getDeclarations()).thenReturn(mock(Iterator.class));
        when(nameInfo2.getDeclarations().hasNext()).thenReturn(true, false);
        when(nameInfo2.getDeclarations().next()).thenReturn(symbol1, symbol2);
        when(symbol2 instanceof Property).thenReturn(true);
        when(symbol2.getValue()).thenReturn(value2);
        when(value2.isFunction()).thenReturn(true);
        when(value2.getParent()).thenReturn(symbolParent2);
        when(symbolParent2.isGetterDef()).thenReturn(false);
        when(symbolParent2.isSetterDef()).thenReturn(false);
        when(symbol2.getPrototype()).thenReturn(symbolProto2);
        when(moduleGraph.dependsOn(any(JSModule.class), any(JSModule.class))).thenReturn(true);

        JavaCompilationUnit javaCu = PowerMockito.mock(JavaCompilationUnit.class);
        PowerMockito.when(javaCu.getAllNameInfo()).thenReturn(allNameInfo);
        crossModuleMethodMotion.moveMethods(allNameInfo);

        // Assert that the necessary methods were called
        verify(javaCu, times(2)).getAllNameInfo();
        verify(moduleGraph, times(2)).dependsOn(any(JSModule.class), any(JSModule.class));
        verify(symbol1).getValue();
        verify(value1).isFunction();
        verify(symbol1).getParent();
        verify(symbolParent1).isGetterDef();
        verify(symbolParent1).isSetterDef();
        verify(symbol2).getValue();
        verify(value2).isFunction();
        verify(symbol2).getParent();
        verify(symbolParent2).isGetterDef();
        verify(symbolParent2).isSetterDef();
    }
}
