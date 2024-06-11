package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodeSetContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UnionContext.class})
public class UnionContextTest {

    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext parentContext;
    private NodeSetContext nodeSetContext;
    private UnionContext unionContext;

    @Before
    public void setup() {
        contextA = Mockito.mock(EvalContext.class);
        contextB = Mockito.mock(EvalContext.class);
        parentContext = Mockito.mock(NodeSetContext.class);
        nodeSetContext = Mockito.mock(NodeSetContext.class);

        List<EvalContext> contexts = new ArrayList<>();
        contexts.add(contextA);
        contexts.add(contextB);

        unionContext = new UnionContext(parentContext, contexts.toArray(new EvalContext[0]));
    }

    @Test
    public void testUnionContextConstructor() {
        assertNotNull(unionContext);
        assertEquals(contexts.length, unionContext.contexts.length);
        assertFalse(unionContext.isPrepared());
    }

    @Test
    public void testGetDocumentOrder() {
        unionContext.setPrepared();
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        NodePointer nodePointerA = Mockito.mock(NodePointer.class);
        NodePointer nodePointerB = Mockito.mock(NodePointer.class);
        nodeSetContext.add(nodePointerA);
        nodeSetContext.add(nodePointerB);
        Mockito.when(contextA.getCurrentNodePointer()).thenReturn(nodePointerA);
        Mockito.when(contextB.getCurrentNodePointer()).thenReturn(nodePointerB);

        unionContext.setPosition(0);
        assertEquals(nodePointerA, unionContext.getNodeSet().getNode(0));

        unionContext.setPosition(1);
        assertEquals(nodePointerB, unionContext.getNodeSet().getNode(0));
    }

    @Test(expected = AssertionError.class)
    public void testSetPositionOutOfBounds() {
        unionContext.setPosition(2);
    }

    @Test
    public void testGetContexts() {
        assertEquals(contexts.size(), unionContext.getContexts.length);
    }

    @Test
    public void testPrepareContext() {
        unionContext.setPrepared();

        NodePointer nodePointerA = Mockito.mock(NodePointer.class);
        NodePointer nodePointerB = Mockito.mock(NodePointer.class);
        Mockito.when(contextA.getNameSpaceURI()).thenReturn("uriA");
        Mockito.when(contextA.getCurrentNodePointer()).thenReturn(nodePointerA);
        Mockito.when(contextB.getNameSpaceURI()).thenReturn("uriB");
        Mockito.when(contextB.getCurrentNodePointer()).thenReturn(nodePointerB);

        unionContext.prepareContext();
        assertEquals("uriA, uriB", unionContext.getUniqueNamespaceURIs());
        assertEquals(nodePointerA, unionContext.getNodeSet().getNode(0));
        assertEquals(nodePointerB, unionContext.getNodeSet().getNode(1));
    }
}