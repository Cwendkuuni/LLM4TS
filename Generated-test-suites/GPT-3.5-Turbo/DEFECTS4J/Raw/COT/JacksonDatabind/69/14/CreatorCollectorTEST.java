
import org.junit.Test;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {
  
  @Test
  public void testConstructValueInstantiator() {
    // Setup
    BeanDescription beanDesc = new BeanDescription();
    MapperConfig<?> config = new MapperConfig<>();
    CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
    DeserializationConfig deserializationConfig = new DeserializationConfig();
    
    // Run the test
    ValueInstantiator result = creatorCollector.constructValueInstantiator(deserializationConfig);
    
    // Verify the results
    assertEquals(expectedResult, result);
  }
  
  @Test
  public void testSetDefaultCreator() {
    // Setup
    AnnotatedWithParams creator = new AnnotatedWithParams();

    // Run the test
    setDefaultCreator(creator);

    // Verify the results
    assertNotEquals(null, creator);
    assertEquals(creator, _creators[C_DEFAULT]);
  }

  @Test
  public void testAddStringCreator() {
    // Setup
    AnnotatedWithParams creator = new AnnotatedWithParams();

    // Run the test
    addStringCreator(creator, false);

    // Verify the results
    assertNotEquals(null, creator);
    assertEquals(creator, _creators[C_STRING]);
  }
  
  @Test
  public void testAddIntCreator() {
    // Setup
    AnnotatedWithParams creator = new AnnotatedWithParams();

    // Run the test
    addIntCreator(creator, false);

    // Verify the results
    assertNotEquals(null, creator);
    assertEquals(creator, _creators[C_INT]);
  }
  
  @Test
  public void testAddLongCreator() {
    // Setup
    AnnotatedWithParams creator = new AnnotatedWithParams();

    // Run the test
    addLongCreator(creator, false);

    // Verify the results
    assertNotEquals(null, creator);
    assertEquals(creator, _creators[C_LONG]);
  }
  
  @Test
  public void testAddDoubleCreator() {
    // Setup
    AnnotatedWithParams creator = new AnnotatedWithParams();

    // Run the test
    addDoubleCreator(creator, false);

    // Verify the results
    assertNotEquals(null, creator);
    assertEquals(creator, _creators[C_DOUBLE]);
  }
  
  @Test
  public void testAddBooleanCreator() {
    // Setup
    AnnotatedWithParams creator = new AnnotatedWithParams();

    // Run the test
    addBooleanCreator(creator, false);

    // Verify the results
    assertNotEquals(null, creator);
    assertEquals(creator, _creators[C_BOOLEAN]);
  }
  
  @Test
  public void testAddDelegatingCreator() {
    // Setup
    AnnotatedWithParams creator = new AnnotatedWithParams();
    boolean explicit = false;
    SettableBeanProperty[] injectables = new SettableBeanProperty();

    // Run the test
    addDelegatingCreator(creator, explicit, injectables);

    // Verify the results
    assertNotEquals(null, creator);
    assertEquals(creator, _creators[C_DELEGATE]);
    assertEquals(injectables, _delegateArgs);
  }
  
  @Test
  public void testAddPropertyCreator() {
    // Setup
    AnnotatedWithParams creator = new AnnotatedWithParams();
    boolean explicit = false;
    SettableBeanProperty[] properties = new SettableBeanProperty();

    // Run the test
    addPropertyCreator(creator, explicit, properties);

    // Verify the results
    assertNotEquals(null, creator);
    assertEquals(creator, _creators[C_PROPS]);
    assertEquals(properties, _propertyBasedArgs);
  }
  
  @Test
  public void testAddIncompletedParameter() {
    // Setup
    AnnotatedParameter parameter = new AnnotatedParameter();

    // Run the test
    addIncompletedParameter(parameter);

    // Verify the results
    assertNotEquals(null, parameter);
    assertEquals(parameter, _incompletedParameter);
  }
}
