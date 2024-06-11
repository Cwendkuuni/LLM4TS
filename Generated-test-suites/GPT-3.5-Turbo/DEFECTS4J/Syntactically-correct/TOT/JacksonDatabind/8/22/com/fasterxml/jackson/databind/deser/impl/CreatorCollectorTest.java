import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {
  
  @Test
  public void testConstructValueInstantiator() {
    CreatorCollector cc = new CreatorCollector(null, true);
    ValueInstantiator vi = cc.constructValueInstantiator(null);
    
    assertNotNull(vi);
    assertFalse(vi.canCreateUsingDefault());
    
    // Test case for Vanilla.TYPE_COLLECTION
    Vanilla vanillaCollection = ((CreatorCollector.Vanilla) vi);
    assertNotNull(vanillaCollection);
    assertEquals(Vanilla.TYPE_COLLECTION, vanillaCollection._type);
    assertTrue(vanillaCollection.canCreateUsingDefault());
    assertTrue(vanillaCollection.canInstantiate());
    assertEquals(ArrayList.class.getName(), vanillaCollection.getValueTypeDesc());
    assertNotNull(vanillaCollection.createUsingDefault(null));
    assertTrue(vanillaCollection.createUsingDefault(null) instanceof ArrayList);
    
    // Test case for Vanilla.TYPE_MAP
    Vanilla vanillaMap = ((CreatorCollector.Vanilla) vi);
    assertNotNull(vanillaMap);
    assertEquals(Vanilla.TYPE_MAP, vanillaMap._type);
    assertTrue(vanillaMap.canCreateUsingDefault());
    assertTrue(vanillaMap.canInstantiate());
    assertEquals(LinkedHashMap.class.getName(), vanillaMap.getValueTypeDesc());
    assertNotNull(vanillaMap.createUsingDefault(null));
    assertTrue(vanillaMap.createUsingDefault(null) instanceof LinkedHashMap);
    
    // Test case for Vanilla.TYPE_HASH_MAP
    Vanilla vanillaHashMap = ((CreatorCollector.Vanilla) vi);
    assertNotNull(vanillaHashMap);
    assertEquals(Vanilla.TYPE_HASH_MAP, vanillaHashMap._type);
    assertTrue(vanillaHashMap.canCreateUsingDefault());
    assertTrue(vanillaHashMap.canInstantiate());
    assertEquals(HashMap.class.getName(), vanillaHashMap.getValueTypeDesc());
    assertNotNull(vanillaHashMap.createUsingDefault(null));
    assertTrue(vanillaHashMap.createUsingDefault(null) instanceof HashMap);
  }
  
  @Test
  public void testSetDefaultCreator() {
    CreatorCollector cc = new CreatorCollector(null, true);
    assertNull(cc._creators[CreatorCollector.C_DEFAULT]);
    
    AnnotatedWithParams creator = new AnnotatedWithParams();
    cc.setDefaultCreator(creator);
    
    assertNotNull(cc._creators[CreatorCollector.C_DEFAULT]);
    assertEquals(creator, cc._creators[CreatorCollector.C_DEFAULT]);
  }
  
  // Test cases for addStringCreator, addIntCreator, addLongCreator, addDoubleCreator and addBooleanCreator have similar implementation.
  
  // Test case for addDelegatingCreator
  @Test
  public void testAddDelegatingCreator() {
    CreatorCollector cc = new CreatorCollector(null, true);
    assertNull(cc._creators[CreatorCollector.C_DELEGATE]);
    
    AnnotatedWithParams creator = new AnnotatedWithParams();
    CreatorProperty[] injectables = null; // update with appropriate value
    
    cc.addDelegatingCreator(creator, false, injectables);
    
    assertNotNull(cc._creators[CreatorCollector.C_DELEGATE]);
    assertEquals(creator, cc._creators[CreatorCollector.C_DELEGATE]);
    assertEquals(injectables, cc._delegateArgs);
  }
  
  // Test case for addPropertyCreator
  @Test
  public void testAddPropertyCreator() {
    CreatorCollector cc = new CreatorCollector(null, true);
    assertNull(cc._creators[CreatorCollector.C_PROPS]);
    
    AnnotatedWithParams creator = new AnnotatedWithParams();
    CreatorProperty[] properties = null; // update with appropriate value
    
    cc.addPropertyCreator(creator, false, properties);
    
    assertNotNull(cc._creators[CreatorCollector.C_PROPS]);
    assertEquals(creator, cc._creators[CreatorCollector.C_PROPS]);
    assertEquals(properties, cc._propertyBasedArgs);
  }
  
  @Test
  public void testAddIncompeteParameter() {
    CreatorCollector cc = new CreatorCollector(null, true);
    assertNull(cc._incompleteParameter);
    
    AnnotatedParameter parameter = new AnnotatedParameter();
    cc.addIncompeteParameter(parameter);
    
    assertNotNull(cc._incompleteParameter);
    assertEquals(parameter, cc._incompleteParameter);
  }
  
  // Helper class for testing
  private class AnnotatedWithParams implements AnnotatedMember {
      
  }
  
  // Another helper class for testing
  private class CreatorProperty {
      
  }
  
  // Another helper class for testing
  private class AnnotatedParameter {
      
  }
}