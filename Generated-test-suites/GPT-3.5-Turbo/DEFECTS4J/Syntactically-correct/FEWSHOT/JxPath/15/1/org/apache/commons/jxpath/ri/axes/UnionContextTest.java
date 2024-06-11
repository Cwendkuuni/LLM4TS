package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UnionContextTest {
    private EvalContext parentContext;
    private EvalContext[] contexts;

    @Before
    public void setUp() {
        parentContext = Mockito.mock(EvalContext.class);
        contexts = new EvalContext[2];
        contexts[0] = Mockito.mock(EvalContext.class);
        contexts[1] = Mockito.mock(EvalContext.class);
    }

    // Test constructor
    @Test
    public void testConstructor() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNotNull(unionContext);
    }

    // Test getDocumentOrder
    @Test
    public void testGetDocumentOrder() {
        contexts[0] = Mockito.mock(EvalContext.class);
        contexts[1] = Mockito.mock(EvalContext.class);

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    // Test setPosition
    @Test
    public void testSetPosition() {
        contexts[0] = Mockito.mock(EvalContext.class);
        contexts[1] = Mockito.mock(EvalContext.class);

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.setPosition(1));
    }
}