package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UnionContextTest {
    @Mock
    private EvalContext contextA;
    @Mock
    private EvalContext contextB;
    @Mock
    private EvalContext parentContext;
    @Mock
    private NodePointer nodeA;
    @Mock
    private NodePointer nodeB;
    @Mock
    private BasicNodeSet nodeSet;

    private UnionContext unionContext;

    @Before
    public void setUp() {
        Mockito.when(contextA.nextSet()).thenReturn(true).thenReturn(false);
        Mockito.when(contextA.nextNode()).thenReturn(nodeA).thenReturn(nodeB).thenReturn(null);
        Mockito.when(contextB.nextSet()).thenReturn(true).thenReturn(false);
        Mockito.when(contextB.nextNode()).thenReturn(nodeB).thenReturn(null);

        Mockito.when(parentContext.getDocumentOrder()).thenReturn(0);
        Mockito.when(parentContext.getPosition()).thenReturn(-1);

        Mockito.when(nodeA.getDocumentOrder()).thenReturn(0);
        Mockito.when(nodeB.getDocumentOrder()).thenReturn(1);

        Mockito.when(nodeSet.add(nodeA)).thenReturn(true);
        Mockito.when(nodeSet.add(nodeB)).thenReturn(true);

        unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});
    }

    @Test
    public void testGetDocumentOrder() {
        Assert.assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_noContexts() {
        unionContext.contexts = new EvalContext[0];
        Assert.assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_singleContext() {
        unionContext.contexts = new EvalContext[]{contextA};
        Assert.assertFalse(unionContext.setPosition(0));
        Mockito.verify(contextA).nextSet();
        Mockito.verify(contextA).nextNode();
        Assert.assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testSetPosition_firstContextExhausted() {
        Mockito.when(contextA.nextSet()).thenReturn(true).thenReturn(false);
        Mockito.when(contextB.nextSet()).thenReturn(true).thenReturn(false);

        Assert.assertFalse(unionContext.setPosition(0));
        Mockito.verify(contextA).nextSet();
        Mockito.verify(contextA).nextNode();
        Mockito.verify(contextB).nextNode();

        Assert.assertTrue(unionContext.setPosition(1));
        Mockito.verify(contextB).nextNode();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_preparedState() {
        unionContext.prepared = true;
        unionContext.setPosition(0);
    }

    @Test
    public void testSetPosition_multipleNodesInFirstContext() {
        Mockito.when(contextA.nextSet()).thenReturn(true).thenReturn(false);
        Mockito.when(contextB.nextSet()).thenReturn(true).thenReturn(false);

        Assert.assertFalse(unionContext.setPosition(0));
        Mockito.verify(contextA).nextSet();
        Mockito.verify(contextA).nextNode();
        Mockito.verify(contextA).nextNode();
        Mockito.verify(contextB).nextNode();

        Assert.assertTrue(unionContext.setPosition(2));
        Mockito.verify(contextB).nextNode();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_positionOutOfRange() {
        unionContext.setPosition(Integer.MAX_VALUE);
    }
}