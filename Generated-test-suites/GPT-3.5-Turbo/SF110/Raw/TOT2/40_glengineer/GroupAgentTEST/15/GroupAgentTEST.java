import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class GroupAgentTest {
  
  @Test
  public void testGetFirstName_EmptyChildren() {
    GroupAgent groupAgent = new GroupAgent();
    assertNull(groupAgent.getFirstName());
  }
  
  @Test
  public void testGetLastName_EmptyChildren() {
    GroupAgent groupAgent = new GroupAgent();
    assertNull(groupAgent.getLastName());
  }
  
  @Test
  public void testIsComponent() {
    GroupAgent groupAgent = new GroupAgent();
    assertFalse(groupAgent.isComponent("TestComponent"));
  }
  
  @Test
  public void testIsGroup() {
    GroupAgent groupAgent = new GroupAgent();
    assertTrue(groupAgent.isGroup("", ""));
  }
  
  @Test
  public void testIsGap() {
    GroupAgent groupAgent = new GroupAgent();
    assertFalse(groupAgent.isGap());
  }
  
  @Test
  public void testEquals() {
    GroupAgent groupAgent = new GroupAgent();
    GroupAgent otherAgent = new GroupAgent();
    assertFalse(groupAgent.equals(otherAgent));
  }
  
  @Test
  public void testAddAgent() {
    GroupAgent groupAgent = new GroupAgent();
    Agent agent = new Agent();
    groupAgent.addAgent(agent);
    assertEquals(1, groupAgent.getNumberOfChildren());
  }
  
  @Test
  public void testGetChildren() {
    GroupAgent groupAgent = new GroupAgent();
    List<Agent> children = groupAgent.getChildren();
    assertNotNull(children);
    assertEquals(0, children.size());
  }
  
  @Test
  public void testGetNumberOfChildren() {
    GroupAgent groupAgent = new GroupAgent();
    assertEquals(0, groupAgent.getNumberOfChildren());
  }
  
  @Test
  public void testGetComponent_ThrowsException() {
    GroupAgent groupAgent = new GroupAgent();
    assertThrows(IllegalArgumentException.class, () -> groupAgent.getComponent("TestComponent"));
  }
  
  @Test
  public void testGetGroup_ThrowsException() {
    GroupAgent groupAgent = new GroupAgent();
    assertThrows(IllegalArgumentException.class, () -> groupAgent.getGroup("First", "Last"));
  }

  // Add additional test cases here for the remaining methods
  
}