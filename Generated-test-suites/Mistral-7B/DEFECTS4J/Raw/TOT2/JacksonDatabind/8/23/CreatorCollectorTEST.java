import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

 private CreatorCollector collector;

 @Before
 public void setup() {
 BeanDescription beanDesc = mock(BeanDescription.class);
 collector = new CreatorCollector(beanDesc, true);
 }

 @Test
 public void testDefaultCreator() {
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
 collector.setDefaultCreator(creator);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 assertTrue(instantiator instanceof StdValueInstantiator);
 }

 @Test
 public void testStringCreator() {
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
 collector.addStringCreator(creator, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 assertTrue(instantiator.canCreateUsingString());
 }

 @Test
 public void testIntCreator() {
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class, "intCreator");
 collector.addIntCreator(creator, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 instantiator.configureFromIntCreator(creator);
 Object result = instantiator.createValueInstance(null);
 assertEquals(0, result.hashCode());
 }

 @Test
 public void testLongCreator() {
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class, "longCreator");
 collector.addLongCreator(creator, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 instantiator.configureFromLongCreator(creator);
 Object result = instantiator.createValueInstance(null);
 assertEquals(0L, result.hashCode());
 }

 @Test
 public void testDoubleCreator() {
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class, "doubleCreator");
 collector.addDoubleCreator(creator, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 instantiator.configureFromDoubleCreator(creator);
 Object result = instantiator.createValueInstance(null);
 assertEquals(0.0, result.hashCode(), 0.0);
 }

 @Test
 public void testBooleanCreator() {
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class, "booleanCreator");
 collector.addBooleanCreator(creator, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 instantiator.configureFromBooleanCreator(creator);
 Object result = instantiator.createValueInstance(null);
 assertTrue(result instanceof Boolean);
 assertEquals(false, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testDuplicateCreators() {
 AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
 collector.addIntCreator(creator1, false);
 AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
 collector.addIntCreator(creator2, false);
 collector.constructValueInstantiator(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCreatorType() {
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class, "invalidCreator");
 collector.addIntCreator(creator, false);
 collector.constructValueInstantiator(null);
 }
}