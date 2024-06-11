import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotated.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

 private MapperFactoryMarker context = new MapperFactoryMarker();

 @Test
 public void testDefaultCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(context.createBeanDescription(TestBean.class), context.getConfig());
 Object value = collector.constructValueInstantiator(context.getConfig()).createValueInstance(null);
 assertTrue(value instanceof TestBean);
 }

 @Test
 public void testStringCreator() throws Exception {
 TestBean bean = new TestBean();
 AnnotatedWithParams stringCreator = context.createMethodParameterAnnotated(TestStringConstructor.class, "stringCreator");
 collector.addStringCreator(stringCreator, false);
 Object value = collector.constructValueInstantiator(context.getConfig()).createValueInstance(null, "test");
 assertEquals("test", value);
 }

 @Test
 public void testIntCreator() throws Exception {
 TestBean bean = new TestBean();
 AnnotatedWithParams intCreator = context.createMethodParameterAnnotated(TestIntConstructor.class, "intCreator");
 collector.addIntCreator(intCreator, false);
 Object value = collector.constructValueInstantiator(context.getConfig()).createValueInstance(null, 42);
 assertEquals(42, value.getIntField());
 }

 @Test
 public void testLongCreator() throws Exception {
 TestBean bean = new TestBean();
 AnnotatedWithParams longCreator = context.createMethodParameterAnnotated(TestLongConstructor.class, "longCreator");
 collector.addLongCreator(longCreator, false);
 Object value = collector.constructValueInstantiator(context.getConfig()).createValueInstance(null, 42L);
 assertEquals(42L, value.getLongField());
 }

 @Test
 public void testDoubleCreator() throws Exception {
 TestBean bean = new TestBean();
 AnnotatedWithParams doubleCreator = context.createMethodParameterAnnotated(TestDoubleConstructor.class, "doubleCreator");
 collector.addDoubleCreator(doubleCreator, false);
 Object value = collector.constructValueInstantiator(context.getConfig()).createValueInstance(null, 42.0);
 assertEquals(42.0, value.getDoubleField(), 0.0);
 }

 @Test
 public void testBooleanCreator() throws Exception {
 TestBean bean = new TestBean();
 AnnotatedWithParams booleanCreator = context.createMethodParameterAnnotated(TestBooleanConstructor.class, "booleanCreator");
 collector.addBooleanCreator(booleanCreator, false);
 Object value = collector.constructValueInstantiator(context.getConfig()).createValueInstance(null, true);
 assertTrue(value.isBooleanField());
 }

 @Test
 public void testDelegateCreator() throws Exception {
 TestDelegateBean delegateBean = new TestDelegateBean();
 AnnotatedWithParams delegateCreator = context.createMethodParameterAnnotated(TestDelegateConstructor.class, "delegateCreator");
 collector.addDelegatingCreator(delegateCreator, false, new SettableBeanProperty[]{new SettableBeanProperty("delegate", delegateBean)});
 TestBean value = (TestBean) collector.constructValueInstantiator(context.getConfig()).createValueInstance(null);
 assertEquals(delegateBean, value.getDelegateField());
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 TestBean bean = new TestBean();
 AnnotatedWithParams propertyCreator = context.createMethodParameterAnnotated(TestPropertyBasedConstructor.class, "propertyCreator");
 SettableBeanProperty[] properties = new SettableBeanProperty[]{new SettableBeanProperty("property1", "value1"), new SettableBeanProperty("property2", "value2")};
 collector.addPropertyCreator(propertyCreator, false, properties);
 Object value = collector.constructValueInstantiator(context.getConfig()).createValueInstance(null);
 assertEquals("value1", value.getProperty1());
 assertEquals("value2", value.getProperty2());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws Exception {
 TestBean bean = new TestBean();
 AnnotatedWithParams stringCreator = context.createMethodParameterAnnotated(TestStringConstructor.class, "stringCreator");
 collector.addStringCreator(stringCreator, false);
 collector.addStringCreator(stringCreator, false);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testIncompatibleCreatorTypes() throws Exception {
 TestBean bean = new TestBean();
 AnnotatedWithParams intCreator = context.createMethodParameterAnnotated(TestLongConstructor.class, "intCreator");
 collector.addIntCreator(intCreator, false);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testNullCreator() throws Exception {
 TestBean bean = new TestBean();
 AnnotatedWithParams nullCreator = null;
 collector.addIntCreator(nullCreator, false);
 }

 @Test
 public void testIncompleteParameter() throws Exception {
 TestBean bean = new TestBean();
 AnnotatedParameter incompleteParameter = context.createAnnotatedParameter(TestIncompleteParameterConstructor.class, 0);
 collector.addIncompleteParameter(incompleteParameter);
 Object value = collector.constructValueInstantiator(context.getConfig()).createValueInstance(null);
 assertNotNull(value);
 }

 @Test
 public void testConstructValueInstantiatorWithNoCreators() throws Exception {
 TestBean bean = new TestBean();
 collector.constructValueInstantiator(context.getConfig());
 }

}

class TestBean {
 private int intField;
 private long longField;
 private double doubleField;
 private boolean booleanField;
 private TestDelegateBean delegateField;
 private String property1;
 private String property2;

 public TestBean() {
 }

 public int getIntField() { return intField; }
 public long getLongField() { return longField; }
 public double getDoubleField() { return doubleField; }
 public boolean isBooleanField() { return booleanField; }
 public TestDelegateBean getDelegateField() { return delegateField; }
 public String getProperty1() { return property1; }
 public String getProperty2() { return property2; }
}

class TestDelegateBean {
 private String delegate;

 public TestDelegateBean() {
 }

 public String getDelegate() { return delegate; }
 public void setDelegate(String delegate) { this.delegate = delegate; }
}

class TestStringConstructor {
 public static TestBean stringCreator(String s) { return new TestBean(); }
}

class TestIntConstructor {
 public static TestBean intCreator(int i) {
 TestBean bean = new TestBean();
 bean.intField = i;
 return bean;
 }
}

class TestLongConstructor {
 public static TestBean longCreator(long l) {
 TestBean bean = new TestBean();
 bean.longField = l;
 return bean;
 }
}

class TestDoubleConstructor {
 public static TestBean doubleCreator(double d) {
 TestBean bean = new TestBean();
 bean.doubleField = d;
 return bean;
 }
}

class TestBooleanConstructor {
 public static TestBean booleanCreator(boolean b) {
 TestBean bean = new TestBean();
 bean.booleanField = b;
 return bean;
 }
}

class TestDelegateConstructor {
 public static TestBean delegateCreator(TestDelegateBean delegate) {
 TestBean bean = new TestBean();
 bean.delegateField = delegate;
 return bean;
 }
}

class TestPropertyBasedConstructor {
 public static TestBean propertyCreator(String property1, String property2) {
 TestBean bean = new TestBean();
 bean.property1 = property1;
 bean.property2 = property2;
 return bean;
 }
}

class TestIncompleteParameterConstructor {
 public static TestBean inCompleteParameter(AnnotatedParameter parameter) {
 TestBean bean = new TestBean();
 return bean;
 }
}