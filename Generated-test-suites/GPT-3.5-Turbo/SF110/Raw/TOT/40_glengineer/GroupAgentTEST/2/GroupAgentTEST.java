import static org.junit.Assert.*;

import org.junit.Test;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        // Test with empty childrenAgents list
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        assertNull(group.getFirstName());

        // Test with childrenAgents list containing one child with null firstName
        GroupAgent group2 = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent child = new Agent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        group2.addAgent(child);
        assertNull(group2.getFirstName());

        // Test with childrenAgents list containing one child with non-null firstName
        GroupAgent group3 = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent child2 = new Agent() {
            @Override
            public String getFirstName() {
                return "John";
            }
        };
        group3.addAgent(child2);
        assertEquals("John", group3.getFirstName());
    }

    @Test
    public void testGetLastName() {
        // Test with empty childrenAgents list
        GroupAgent group = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        assertNull(group.getLastName());

        // Test with childrenAgents list containing one child with null lastName
        GroupAgent group2 = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent child = new Agent() {
            @Override
            public String getLastName() {
                return null;
            }
        };
        group2.addAgent(child);
        assertNull(group2.getLastName());

        // Test with childrenAgents list containing one child with non-null lastName
        GroupAgent group3 = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent child2 = new Agent() {
            @Override
            public String getLastName() {
                return "Doe";
            }
        };
        group3.addAgent(child2);
        assertEquals("Doe", group3.getLastName());
    }
    
    // Test for other methods in GroupAgent class

    // ...

}