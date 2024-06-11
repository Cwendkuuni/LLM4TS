package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class JSJshopTest {
    private JSJshop jsjShop;
    private JSPlanningDomain mockDom;
    private JSPlanningProblem mockProb;
    private JSListPlanningProblem mockProbSet;
    private JSPlan mockSol;
    private JSJshopNode mockTree;
    private JSPairPlanTSListNodes mockSolution;

    @Before
    public void setup() {
        jsjShop = new JSJshop();
        mockDom = mock(JSPlanningDomain.class);
        mockProb = mock(JSPlanningProblem.class);
        mockProbSet = mock(JSListPlanningProblem.class);
        mockSol = mock(JSPlan.class);
        mockTree = mock(JSJshopNode.class);
        mockSolution = mock(JSPairPlanTSListNodes.class);
    }

    @Test
    public void testConstructor() {
        assertNull(jsjShop.prob);
        assertNull(jsjShop.probSet);
        assertNull(jsjShop.tree);

        jsjShop = new JSJshop("domainFile", "problemFile");

        assertNull(jsjShop.prob);
        assertEquals(mockProbSet, jsjShop.probSet);
        assertNull(jsjShop.tree);
    }

    @Test
    public void testConstructorWithFiles() {
        assertNotNull(jsjShop.probSet);
        assertNull(jsjShop.tree);

        jsjShop = new JSJshop("domainFile", "problemFile");

        assertNull(jsjShop.tree);
    }

    @Test
    public void testGetTree() {
        jsjShop.tree = mockTree;

        assertEquals(mockTree, jsjShop.getTree());
    }

    @Test
    public void testGetSolution() {
        jsjShop.solution = mockSolution;

        assertEquals(mockSolution, jsjShop.getSolution());
    }

    @Test
    public void testGetAddList() {
        JSTState mockTState = mock(JSTState.class);
        JSListLogicalAtoms mockAddList = mock(JSListLogicalAtoms.class);
        when(mockSolution.planS()).thenReturn(mockTState);
        when(mockTState.addList()).thenReturn(mockAddList);

        assertEquals(mockAddList, jsjShop.getAddList());
    }

    @Test
    public void testGetDeleteList() {
        JSTState mockTState = mock(JSTState.class);
        JSListLogicalAtoms mockDeleteList = mock(JSListLogicalAtoms.class);
        when(mockSolution.planS()).thenReturn(mockTState);
        when(mockTState.deleteList()).thenReturn(mockDeleteList);

        assertEquals(mockDeleteList, jsjShop.getDeleteList());
    }

    @Test
    public void testSetFile() {
        JSTaskAtom mockTaskAtom = mock(JSTaskAtom.class);
        Vector<JSJshopNode> mockListNodes = mock(Vector.class);

        when(mockSolution.listNodes()).thenReturn(mockListNodes);
        when(mockListNodes.elementAt(0)).thenReturn(mockTree);

        assertEquals(mockSolution, jsjShop.setFile("fileName", mockTaskAtom));
        assertEquals(mockTree, jsjShop.tree);
    }
}