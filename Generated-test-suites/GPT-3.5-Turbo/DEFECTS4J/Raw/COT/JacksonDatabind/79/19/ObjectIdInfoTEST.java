import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

  @Test
  public void testConstructor1() {
    PropertyName prop = new PropertyName("name");
    Class<?> scope = String.class;
    Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen, resolver);

    Assert.assertEquals(prop, objectIdInfo.getPropertyName());
    Assert.assertEquals(scope, objectIdInfo.getScope());
    Assert.assertEquals(gen, objectIdInfo.getGeneratorType());
    Assert.assertEquals(resolver, objectIdInfo.getResolverType());
    Assert.assertFalse(objectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testConstructor2() {
    PropertyName prop = new PropertyName("name");
    Class<?> scope = String.class;
    Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen);

    Assert.assertEquals(prop, objectIdInfo.getPropertyName());
    Assert.assertEquals(scope, objectIdInfo.getScope());
    Assert.assertEquals(gen, objectIdInfo.getGeneratorType());
    Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    Assert.assertFalse(objectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testConstructor3() {
    PropertyName prop = new PropertyName("name");
    Class<?> scope = String.class;
    Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;
    String name = "resolver";

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);

    Assert.assertEquals(prop, objectIdInfo.getPropertyName());
    Assert.assertEquals(scope, objectIdInfo.getScope());
    Assert.assertEquals(gen, objectIdInfo.getGeneratorType());
    Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    Assert.assertFalse(objectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testWithAlwaysAsId() {
    PropertyName prop = new PropertyName("name");
    Class<?> scope = String.class;
    Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen, resolver);
    ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

    Assert.assertEquals(prop, updatedObjectIdInfo.getPropertyName());
    Assert.assertEquals(scope, updatedObjectIdInfo.getScope());
    Assert.assertEquals(gen, updatedObjectIdInfo.getGeneratorType());
    Assert.assertEquals(resolver, updatedObjectIdInfo.getResolverType());
    Assert.assertTrue(updatedObjectIdInfo.getAlwaysAsId());
  }

  public class MyObjectIdGenerator implements ObjectIdGenerator<String> {
    // implementation
  }

  public class MyObjectIdResolver implements ObjectIdResolver {
    // implementation
  }
}