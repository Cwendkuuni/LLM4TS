import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

  private ObjectIdInfo objectIdInfo;

  @Before
  public void setUp() {
    PropertyName propertyName = new PropertyName("testProperty");
    Class<Class> scope = Class.class;
    Class<SimpleObjectIdResolver> generator = SimpleObjectIdResolver.class;
    Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
    objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
  }

  @Test
  public void testConstructor() {
    assertNotNull(objectIdInfo);
    assertEquals("testProperty", objectIdInfo.getPropertyName().getSimpleName());
    assertEquals(Class.class, objectIdInfo.getScope());
    assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getGeneratorType());
    assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    assertFalse(objectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testWithAlwaysAsId() {
    assertFalse(objectIdInfo.getAlwaysAsId());

    ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
    assertNotEquals(objectIdInfo, newObjectIdInfo);
    assertTrue(newObjectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testToString() {
    assertEquals("ObjectIdInfo: propName=testProperty, scope=java.lang.Class, generatorType=com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver, alwaysAsId=false",
        objectIdInfo.toString());
  }

}