package com.google.javascript.jscomp;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    @Before
    public void setUp() {
        // TODO: Set up any necessary objects before each test
    }
    
    @Test
    public void testCreateScope_functionNode_returnsScope() {
        // TODO: Write test case for createScope function when input is a function node
    }
    
    @Test
    public void testCreateScope_nonFunctionNode_returnsScope() {
        // TODO: Write test case for createScope function when input is a non-function node
    }
    
    @Test
    public void testScanRoot_functionNode_scansVariables() {
        // TODO: Write test case for scanRoot function when input is a function node
    }
    
    @Test
    public void testScanRoot_globalBlock_scansVariables() {
        // TODO: Write test case for scanRoot function when input is a global block
    }
    
    @Test
    public void testScanVars_varNode_declaresVariables() {
        // TODO: Write test case for scanVars function when input is a var node
    }
    
    @Test
    public void testScanVars_functionNode_doesNotDeclareVariables() {
        // TODO: Write test case for scanVars function when input is a function node
    }
    
    @Test
    public void testScanVars_catchNode_declaresVariable() {
        // TODO: Write test case for scanVars function when input is a catch node
    }
    
    @Test
    public void testScanVars_scriptNode_setsSourceName() {
        // TODO: Write test case for scanVars function when input is a script node
    }
    
    @Test
    public void testDeclareVar_newVariable_declaresVariable() {
        // TODO: Write test case for declareVar function when variable is new
    }
    
    @Test
    public void testDeclareVar_redeclaredVariable_callsRedeclarationHandler() {
        // TODO: Write test case for declareVar function when variable is redeclared
    }   
}