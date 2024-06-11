package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
    
    private AbstractCompiler compiler;
    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private boolean canModifyExterns;
    private JSModuleGraph moduleGraph;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        canModifyExterns = true;
        moduleGraph = new JSModuleGraph();
    }
    
    @Test
    public void testConstructor() {
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        assertEquals(compiler, crossModuleMethodMotion.compiler);
        assertEquals(idGenerator, crossModuleMethodMotion.idGenerator);
        assertTrue(crossModuleMethodMotion.analyzer instanceof AnalyzePrototypeProperties);
        assertEquals(moduleGraph, crossModuleMethodMotion.moduleGraph);
    }
    
    @Test
    public void testProcess() {
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        Node externRoot = new Node();
        Node root = new Node();
        crossModuleMethodMotion.process(externRoot, root);
    }
    
    @Test
    public void testMoveMethods() {
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        NameInfo nameInfo = new NameInfo();
        Symbol symbol = new Property();
        symbol.setValue(new Node());
        symbol.setModule(new JSModule());
        nameInfo.getDeclarations().add(symbol);
        crossModuleMethodMotion.moveMethods(nameInfo);
    }
    
    @Test
    public void testMoveMethods_WhenDeepestCommonModuleRefIsNull() {
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        NameInfo nameInfo = new NameInfo();
        Symbol symbol = new Property();
        symbol.setValue(new Node());
        symbol.setModule(null);
        nameInfo.getDeclarations().add(symbol);
        crossModuleMethodMotion.moveMethods(nameInfo);
    }
    
    @Test
    public void testMoveMethods_WhenValueIsNotFunction() {
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        NameInfo nameInfo = new NameInfo();
        Symbol symbol = new Property();
        symbol.setValue(new Node("testValue"));
        symbol.setModule(new JSModule());
        nameInfo.getDeclarations().add(symbol);
        crossModuleMethodMotion.moveMethods(nameInfo);
    }
    
    @Test
    public void testMoveMethods_WhenDeepestCommonModuleRefIsNotNull() {
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        NameInfo nameInfo = new NameInfo();
        Symbol symbol = new Property();
        symbol.setValue(new Node("testValue"));
        symbol.setModule(new JSModule());
        nameInfo.getDeclarations().add(symbol);
        crossModuleMethodMotion.moveMethods(nameInfo);
    }
    
    @Test
    public void testIdGenerator() {
        CrossModuleMethodMotion.IdGenerator generator = new CrossModuleMethodMotion.IdGenerator();
        assertTrue(!generator.hasGeneratedAnyIds());
        assertEquals(0, generator.newId());
        assertTrue(generator.hasGeneratedAnyIds());
    }
}