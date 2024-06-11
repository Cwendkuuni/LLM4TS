package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.CrossModuleMethodMotion.IdGenerator;
import com.google.javascript.jscomp.JSModule;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() {
        crossModuleMethodMotion = new CrossModuleMethodMotion(null, null, false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(crossModuleMethodMotion);
    }

    @Test
    public void testHasGeneratedAnyIdsFalse() {
        IdGenerator idGenerator = crossModuleMethodMotion.new IdGenerator();
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testHasGeneratedAnyIdsTrue() {
        IdGenerator idGenerator = crossModuleMethodMotion.new IdGenerator();
        idGenerator.currentId = 1;
        assertTrue(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testNewId() {
        IdGenerator idGenerator = crossModuleMethodMotion.new IdGenerator();
        int id = idGenerator.newId();
        assertEquals(0, id);
    }

    @Test
    public void testProcess() {
        // TO-DO: Write test case for process method
    }

    @Test
    public void testMoveMethods() {
        // TO-DO: Write test case for moveMethods method
    }
}