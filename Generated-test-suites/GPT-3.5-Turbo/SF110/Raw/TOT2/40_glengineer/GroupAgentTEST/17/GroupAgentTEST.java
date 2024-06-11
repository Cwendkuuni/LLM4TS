import static org.junit.Assert.*;
import org.junit.Test;

public class GroupAgentTest {
  
  @Test
  public void testGetFirstName() {
    // Test case for empty childrenAgents list
    GroupAgent groupAgent = new GroupAgent();
    assertNull(groupAgent.getFirstName());
    
    // Test case for non-empty childrenAgents list
    Agent child = new Agent();
    child.setFirstName("John");
    groupAgent.addAgent(child);
    assertEquals("John", groupAgent.getFirstName());
  }
  
  @Test
  public void testGetLastName() {
    // Test case for empty childrenAgents list
    GroupAgent groupAgent = new GroupAgent();
    assertNull(groupAgent.getLastName());
    
    // Test case for non-empty childrenAgents list
    Agent child = new Agent();
    child.setLastName("Doe");
    groupAgent.addAgent(child);
    assertEquals("Doe", groupAgent.getLastName());
  }
  
  @Test
  public void testIsGroup() {
    GroupAgent groupAgent = new GroupAgent();
    Agent child = new Agent();
    child.setFirstName("John");
    child.setLastName("Doe");
    groupAgent.addAgent(child);
    
    assertTrue(groupAgent.isGroup("John", "Doe"));
    assertFalse(groupAgent.isGroup("Jane", "Smith"));
  }
  
  // Add more test cases for other methods
  
}