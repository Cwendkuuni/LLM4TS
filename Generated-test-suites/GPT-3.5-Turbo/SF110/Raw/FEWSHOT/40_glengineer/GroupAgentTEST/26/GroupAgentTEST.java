
package glengineer.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
    public void testGetFirstName() {
        assertEquals(null, groupAgent.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals(null, groupAgent.getLastName());
    }

    @Test
    public void testIsComponent() {
        assertFalse(groupAgent.isComponent("componentName"));
    }

    @Test
    public void testIsGroup() {
        assertFalse(groupAgent.isGroup("firstName", "lastName"));
    }

    @Test
    public void testIsGap() {
        assertFalse(groupAgent.isGap());
    }

    @Test
    public void testEquals() {
        assertFalse(groupAgent.equals(groupAgent));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAgent() {
        groupAgent.addAgent(null);
    }

    @Test
    public void testGetChildren() {
        List<Agent> children = groupAgent.getChildren();
        assertNotNull(children);
        assertTrue(children.isEmpty());
    }

    @Test
    public void testGetNumberOfChildren() {
        int numberOfChildren = groupAgent.getNumberOfChildren();
        assertEquals(0, numberOfChildren);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent() {
        groupAgent.getComponent("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroup() {
        groupAgent.getGroup("firstName", "lastName");
    }

    @Test
    public void testFindDependingComponentByName() {
        ComponentAgent component = groupAgent.findDependingComponentByName("componentName");
        assertEquals(null, component);
    }

    @Test
    public void testFindDependingGroupByNames() {
        GroupAgent group = groupAgent.findDependingGroupByNames("firstName", "lastName");
        assertEquals(null, group);
    }

    @Test
    public void testFindDependingParallelGroupByNames() {
        ParallelGroupAgent parallelGroup = groupAgent.findDependingParallelGroupByNames("firstName", "lastName");
        assertEquals(null, parallelGroup);
    }

    @Test
    public void testFindDependingSequentialGroupByNames() {
        SequentialGroupAgent sequentialGroup = groupAgent.findDependingSequentialGroupByNames("firstName", "lastName");
        assertEquals(null, sequentialGroup);
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
        GroupAgent parentGroup = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };

        GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder() {
            @Override
            public void addGroupToGroup(GroupLayout.Group group, Settings settings, GroupLayout.Group parentGroup) {
                // Add group to parent group
            }
        };

        groupAgent.addContentToGroup(parentGroup.groupContent(creatorAndAdder), creatorAndAdder);
    }

    @Test
    public void testAddGapBefore() {
        GapAgent gap = new GapAgent(10);
        Agent element = new Agent() {};

        groupAgent.addGapBefore(gap, element);
    }

    @Test
    public void testAddGapAfter() {
        GapAgent gap = new GapAgent(10);
        Agent element = new Agent() {};

        groupAgent.addGapAfter(gap, element);
    }

    @Test
    public void testFunctionsOnGroupAndElementImplementation() {
        GroupAgent parent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };

        Agent element = new Agent() {};

        GroupAgent.FunctionsOnGroupAndElementImplementation functions = new GroupAgent.FunctionsOnGroupAndElementImplementation(parent, element);

        functions.addPrecedingGap(10);
        functions.addPrecedingGap(10, 20, 30);
        functions.addFollowingGap(10);
        functions.addFollowingGap(10, 20, 30);
    }
}
