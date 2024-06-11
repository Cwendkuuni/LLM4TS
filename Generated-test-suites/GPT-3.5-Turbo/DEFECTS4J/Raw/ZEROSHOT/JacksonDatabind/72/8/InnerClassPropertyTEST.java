import org.junit.Test;

public class InnerClassPropertyTest {

  @Test
  public void testConstructor1() {
    SettableBeanProperty delegate = new SettableBeanProperty();
    Constructor<?> ctor = new Constructor<?>();
    InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
    // Add asserts here
  }

  @Test
  public void testConstructor2() {
    InnerClassProperty src = new InnerClassProperty();
    AnnotatedConstructor ann = new AnnotatedConstructor();
    InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);
    // Add asserts here
  }

  @Test
  public void testConstructor3() {
    InnerClassProperty src = new InnerClassProperty();
    JsonDeserializer<?> deser = new JsonDeserializer<?>();
    InnerClassProperty innerClassProperty = new InnerClassProperty(src, deser);
    // Add asserts here
  }

  @Test
  public void testConstructor4() {
    InnerClassProperty src = new InnerClassProperty();
    PropertyName newName = new PropertyName();
    InnerClassProperty innerClassProperty = new InnerClassProperty(src, newName);
    // Add asserts here
  }

  @Test
  public void testWithName() {
    PropertyName newName = new PropertyName();
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    InnerClassProperty result = innerClassProperty.withName(newName);
    // Add asserts here
  }

  @Test
  public void testWithValueDeserializer() {
    JsonDeserializer<?> deser = new JsonDeserializer<?>();
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    InnerClassProperty result = innerClassProperty.withValueDeserializer(deser);
    // Add asserts here
  }

  @Test
  public void testAssignIndex() {
    int index = 0;
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    innerClassProperty.assignIndex(index);
    // Add asserts here
  }

  @Test
  public void testGetPropertyIndex() {
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    int result = innerClassProperty.getPropertyIndex();
    // Add asserts here
  }

  @Test
  public void testGetAnnotation() {
    Class<A> acls = A.class;
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    A result = innerClassProperty.getAnnotation(acls);
    // Add asserts here
  }

  @Test
  public void testGetMember() {
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    AnnotatedMember result = innerClassProperty.getMember();
    // Add asserts here
  }

  @Test
  public void testDeserializeAndSet() {
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object bean = new Object();
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    // Add asserts here
  }

  @Test
  public void testDeserializeSetAndReturn() {
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object instance = new Object();
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
    // Add asserts here
  }

  @Test
  public void testSet() {
    Object instance = new Object();
    Object value = new Object();
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    innerClassProperty.set(instance, value);
    // Add asserts here
  }

  @Test
  public void testSetAndReturn() {
    Object instance = new Object();
    Object value = new Object();
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    Object result = innerClassProperty.setAndReturn(instance, value);
    // Add asserts here
  }

  @Test
  public void testReadResolve() {
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    Object result = innerClassProperty.readResolve();
    // Add asserts here
  }

  @Test
  public void testWriteReplace() {
    InnerClassProperty innerClassProperty = new InnerClassProperty();
    Object result = innerClassProperty.writeReplace();
    // Add asserts here
  }
}