import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

  @Test
  public void testSetDefaultCreator() {
    CreatorCollector collector = new CreatorCollector(null, false);
    
    assertNull(collector._creators[0]);
    
    SimpleBean bean = new SimpleBean();
    collector.setDefaultCreator(bean);
    
    assertNotNull(collector._creators[0]);
    assertEquals(bean, collector._creators[0]);
  }
  
  @Test
  public void testAddStringCreator() {
    CreatorCollector collector = new CreatorCollector(null, false);
    
    assertNull(collector._creators[1]);
    
    SimpleBean bean = new SimpleBean();
    collector.addStringCreator(bean, true);
    
    assertNotNull(collector._creators[1]);
    assertEquals(bean, collector._creators[1]);
  }
  
  @Test
  public void testAddIntCreator() {
    CreatorCollector collector = new CreatorCollector(null, false);
    
    assertNull(collector._creators[2]);
    
    SimpleBean bean = new SimpleBean();
    collector.addIntCreator(bean, true);
    
    assertNotNull(collector._creators[2]);
    assertEquals(bean, collector._creators[2]);
  }
  
  @Test
  public void testAddLongCreator() {
    CreatorCollector collector = new CreatorCollector(null, false);
    
    assertNull(collector._creators[3]);
    
    SimpleBean bean = new SimpleBean();
    collector.addLongCreator(bean, true);
    
    assertNotNull(collector._creators[3]);
    assertEquals(bean, collector._creators[3]);
  }
  
  @Test
  public void testAddDoubleCreator() {
    CreatorCollector collector = new CreatorCollector(null, false);
    
    assertNull(collector._creators[4]);
    
    SimpleBean bean = new SimpleBean();
    collector.addDoubleCreator(bean, true);
    
    assertNotNull(collector._creators[4]);
    assertEquals(bean, collector._creators[4]);
  }
  
  @Test
  public void testAddBooleanCreator() {
    CreatorCollector collector = new CreatorCollector(null, false);
    
    assertNull(collector._creators[5]);
    
    SimpleBean bean = new SimpleBean();
    collector.addBooleanCreator(bean, true);
    
    assertNotNull(collector._creators[5]);
    assertEquals(bean, collector._creators[5]);
  }
  
  @Test
  public void testAddDelegatingCreator() {
    CreatorCollector collector = new CreatorCollector(null, false);
    
    assertNull(collector._creators[6]);
    
    SimpleBean bean = new SimpleBean();
    collector.addDelegatingCreator(bean, true, null);
    
    assertNotNull(collector._creators[6]);
    assertEquals(bean, collector._creators[6]);
  }
  
  @Test
  public void testAddPropertyCreator() {
    CreatorCollector collector = new CreatorCollector(null, false);
    
    assertNull(collector._creators[7]);
    
    SimpleBean bean = new SimpleBean();
    collector.addPropertyCreator(bean, true, null);
    
    assertNotNull(collector._creators[7]);
    assertEquals(bean, collector._creators[7]);
  }
  
  @Test
  public void testAddIncompeteParameter() {
    CreatorCollector collector = new CreatorCollector(null, false);
    
    assertNull(collector._incompleteParameter);
    
    AnnotatedParameter param = new AnnotatedParameter(null, null, 1);
    collector.addIncompeteParameter(param);
    
    assertNotNull(collector._incompleteParameter);
    assertEquals(param, collector._incompleteParameter);
  }
  
  @Test
  public void testHasDefaultCreator() {
    CreatorCollector collector = new CreatorCollector(null, false);
    
    assertFalse(collector.hasDefaultCreator());
    
    SimpleBean bean = new SimpleBean();
    collector.setDefaultCreator(bean);
    
    assertTrue(collector.hasDefaultCreator());
  }
  
}