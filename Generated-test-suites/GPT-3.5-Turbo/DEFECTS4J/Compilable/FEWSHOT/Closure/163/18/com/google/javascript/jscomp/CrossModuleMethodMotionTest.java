package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

    @Test
    public void testProcess() {
        // TODO: Implement test case
    }

    @Test
    public void testMoveMethods() {
        // TODO: Implement test case
    }

    @Test
    public void testIdGenerator() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        assertEquals(false, idGenerator.hasGeneratedAnyIds());
        assertEquals(0, idGenerator.newId());
        assertEquals(true, idGenerator.hasGeneratedAnyIds());
    }
}