package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Property;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Symbol;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {
    
    private AbstractCompiler compiler;
    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private boolean canModifyExterns;
    private JSModuleGraph moduleGraph;
    private AnalyzePrototypeProperties analyzer;
    private CrossModuleMethodMotion classUnderTest;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        canModifyExterns = false;
        moduleGraph = new JSModuleGraph();
        analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph,
            canModifyExterns, false);
        classUnderTest = new CrossModuleMethodMotion(compiler, idGenerator,
            canModifyExterns);
    }

    @Test
    public void testProcess_withNullDeepestCommonModuleRef_reportsError() {
        // Arrange
        Node externRoot = IR.root();
        Node root = IR.root();

        analyzer.process(externRoot, root);
        Collection<NameInfo> allNameInfo = analyzer.getAllNameInfo();
        allNameInfo.iterator().next().setDeepestCommonModuleRef(null);
        TestErrorReporter testReporter = new TestErrorReporter();
        compiler.setErrorReporter(testReporter);
        
        // Act
        classUnderTest.process(externRoot, root);
        
        // Assert
        assertTrue(testReporter.hasError(CrossModuleMethodMotion.NULL_COMMON_MODULE_ERROR));
    }
    
    @Test
    public void testProcess_withReferencedAndUnreferencedNameInfo_doNotMoveUnreferencedProperties() {
        // Arrange
        Node externRoot = IR.root();
        Node root = IR.root();
        
        NameInfo unreferencedNameInfo = new NameInfo("name");
        NameInfo referencedNameInfo = new NameInfo("name");
        referencedNameInfo.setReferenced(true);
        AnalyzerTestInputGenerator.setPropertyValueToSymbol(
            new Property(IR.function(IR.name("name")), null), referencedNameInfo);
        
        analyzer.process(externRoot, root);
        Collection<NameInfo> allNameInfo = analyzer.getAllNameInfo();
        allNameInfo.add(unreferencedNameInfo);
        allNameInfo.add(referencedNameInfo);
        unreferencedNameInfo.setDeepestCommonModuleRef(moduleGraph.createModule("module1"));
        referencedNameInfo.setDeepestCommonModuleRef(moduleGraph.createModule("module2"));

        // Act
        classUnderTest.process(externRoot, root);

        // Assert
        assertFalse(unreferencedNameInfo.isMoved());
    }
    
    @Test
    public void testProcess_withUnreferencedNameInfo_doesNotMoveProperties() {
        // Arrange
        Node externRoot = IR.root();
        Node root = IR.root();
        
        NameInfo nameInfo = new NameInfo("name");
        
        analyzer.process(externRoot, root);
        Collection<NameInfo> allNameInfo = analyzer.getAllNameInfo();
        allNameInfo.add(nameInfo);
        nameInfo.setDeepestCommonModuleRef(null);
        
        // Act
        classUnderTest.process(externRoot, root);
        
        // Assert
        assertFalse(nameInfo.isMoved());
    }
    
    @Test
    public void testProcess_withReferencedNameInfoThatReadsClosureVariables_doesNotMoveProperties() {
        // Arrange
        Node externRoot = IR.root();
        Node root = IR.root();
        
        NameInfo nameInfo = new NameInfo("name");
        nameInfo.setReferenced(true);
        nameInfo.setReadsClosureVariables(true);
        
        analyzer.process(externRoot, root);
        Collection<NameInfo> allNameInfo = analyzer.getAllNameInfo();
        allNameInfo.add(nameInfo);
        nameInfo.setDeepestCommonModuleRef(moduleGraph.createModule("module"));
        
        // Act
        classUnderTest.process(externRoot, root);
        
        // Assert
        assertFalse(nameInfo.isMoved());
    }
    
    @Test
    public void testProcess_withReferencedNameInfoAndPropertyThatShouldBeMoved_movesPropertyToDeeperModule() {
        // Arrange
        Node externRoot = IR.root();
        Node root = IR.root();
        
        NameInfo nameInfo = new NameInfo("name");
        nameInfo.setReferenced(true);
        analyzer.process(externRoot, root);
        Collection<NameInfo> allNameInfo = analyzer.getAllNameInfo();
        allNameInfo.add(nameInfo);
        moduleGraph.addModule("module1");
        moduleGraph.addModule("module2");
        moduleGraph.addDependency("module1", "module2");
        JSModule deepestCommonModuleRef = moduleGraph.createModule("module1");
        nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);
        Symbol symbol = new Property(IR.function(IR.name("name")), null);
        symbol.setModule(deepestCommonModuleRef);
        AnalyzerTestInputGenerator.setPropertyValueToSymbol((Property)symbol, nameInfo);
        
        // Act
        classUnderTest.process(externRoot, root);
        
        // Assert
        assertTrue(nameInfo.isMoved());
    }
    
    @Test
    public void testMoveMethods_withDeclarations_replacesPropertyValueWithStubCallAndAddsUnstubCallToDeeperModule() {
        // Arrange
        Collection<NameInfo> allNameInfo = null; // TODO - set this variable with proper value
        CrossModuleMethodMotion classUnderTest = null; // TODO - set this variable with proper value
        Node externRoot = IR.root();
        Node root = IR.root();
        classUnderTest.process(externRoot, root);
        Property property = new Property(IR.function(IR.name("property")), null);
        Node propertyValue = property.getValue();
        Symbol symbol = property;
        symbol.setModule(moduleGraph.createModule("module"));
        AnalyzerTestInputGenerator.setPropertyValueToSymbol(property, symbol);
        allNameInfo.add(new NameInfo("name"));
        idGenerator.newId();
        
        // Act
        classUnderTest.moveMethods(allNameInfo);
        
        // Assert
        assertTrue(propertyValue.getParent().isCall());
        assertTrue(compiler.getNodeForCodeInsertion(null)
            .getLastChild().isExprResult());
    }
    
    private class TestErrorReporter implements ErrorReporter {

        private final List<JSError> errors = new ArrayList<>();

        @Override
        public void report(CheckLevel level, JSError error) {
            errors.add(error);
        }

        public boolean hasError(DiagnosticType errorType) {
            for (JSError error : errors) {
                if (error.getType().key.equals(errorType.key)) {
                    return true;
                }
            }
            return false;
        }
    }
}