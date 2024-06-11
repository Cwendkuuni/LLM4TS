import org.junit.Test;
import static org.junit.Assert.*;

public class GroupAgentTest {
    
    @Test
    public void testGetFirstName_NullWhenNoChildren() {
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName_NullWhenNoChildren() {
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getLastName());
    }
    
    @Test
    public void testIsComponent_ReturnsFalse() {
        GroupAgent groupAgent = new GroupAgent();
        assertFalse(groupAgent.isComponent("test"));
    }
    
    @Test
    public void testIsGroup_TrueIfFirstAndLastNamesMatch() {
        GroupAgent groupAgent = new GroupAgent();
        assertTrue(groupAgent.isGroup(null, null));
    }
    
    @Test
    public void testIsGap_ReturnsFalse() {
        GroupAgent groupAgent = new GroupAgent();
        assertFalse(groupAgent.isGap());
    }
    
    @Test
    public void testEquals_ReturnsTrueForSameGroup() {
        GroupAgent groupAgent = new GroupAgent();
        assertTrue(groupAgent.equals(groupAgent));
    }
    
    @Test
    public void testAddAgent_ThrowsExceptionForNullAgent() {
        GroupAgent groupAgent = new GroupAgent();
        assertThrows(IllegalArgumentException.class, () -> groupAgent.addAgent(null));
    }
    
    // More test cases for the remaining methods...
    
}