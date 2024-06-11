
import glengineer.agents.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import glengineer.*;

public class GroupAgentTest {
    private GroupAgent groupAgent;

    @Before
    public void setUp() {
        groupAgent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
    }

    @Test
    public void testGetFirstNameEmpty() {
        String firstName = groupAgent.getFirstName();
        Assert.assertNull(firstName);
    }

    @Test
    public void testGetLastNameEmpty() {
        String lastName = groupAgent.getLastName();
        Assert.assertNull(lastName);
    }

    @Test
    public void testIsComponent() {
        boolean isComponent = groupAgent.isComponent("component");
        Assert.assertFalse(isComponent);
    }

    @Test
    public void testIsGroup() {
        boolean isGroup = groupAgent.isGroup("firstName", "lastName");
        Assert.assertFalse(isGroup);
    }

    @Test
    public void testIsGap() {
        boolean isGap = groupAgent.isGap();
        Assert.assertFalse(isGap);
    }

    @Test
    public void testEquals() {
        Agent agent = new Agent() {
            @Override
            public String getFirstName() {
                return "firstName";
            }

            @Override
            public String getLastName() {
                return "lastName";
            }
        };
        boolean equals = groupAgent.equals(agent);
        Assert.assertFalse(equals);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAgentWithNullAgent() {
        groupAgent.addAgent(null);
    }

    @Test
    public void testGetChildren() {
        int numberOfChildren = groupAgent.getNumberOfChildren();
        Assert.assertEquals(0, numberOfChildren);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponentNotFound() {
        groupAgent.getComponent("component");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroupNotFound() {
        groupAgent.getGroup("firstName", "lastName");
    }

    @Test
    public void testFindDependingComponentByName() {
        ComponentAgent result = groupAgent.findDependingComponentByName("component");
        Assert.assertNull(result);
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent result = groupAgent.findDependingGroupByNames("firstName", "lastName");
        Assert.assertNull(result);
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        ParallelGroupAgent result = groupAgent.findDependingParallelGroupByNames("firstName", "lastName");
        Assert.assertNull(result);
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        SequentialGroupAgent result = groupAgent.findDependingSequentialGroupByNames("firstName", "lastName");
        Assert.assertNull(result);
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        groupAgent.removeGapsFromParallelGroups();
    }

    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
        groupAgent.replaceTemporaryGapsByRealGaps(map);
    }

    @Test
    public void testAddContentToGroup() {
        GroupLayout.Group parentGroup = null;
        GroupCreatorAndElementAdder creatorAndAdder = null;
        groupAgent.addContentToGroup(parentGroup, creatorAndAdder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapBeforeElementNotFound() {
        GapAgent gap = new GapAgent(1);
        Agent element = new Agent() {
            @Override
            public String toString() {
                return "element";
            }
        };
        groupAgent.addGapBefore(gap, element);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGapAfterElementNotFound() {
        GapAgent gap = new GapAgent(1);
        Agent element = new Agent() {
            @Override
            public String toString() {
                return "element";
            }
        };
        groupAgent.addGapAfter(gap, element);
    }
}
