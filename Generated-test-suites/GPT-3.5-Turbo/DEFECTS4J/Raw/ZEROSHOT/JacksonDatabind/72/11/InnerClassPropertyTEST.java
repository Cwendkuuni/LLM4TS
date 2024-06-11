import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InnerClassPropertyTest {

  private InnerClassProperty innerClassProperty;

  @Before
  public void setUp() {
    innerClassProperty = new InnerClassProperty(null, null);
  }

  @Test
  public void testConstructor() {
    SettableBeanProperty delegate = null;
    Constructor<?> ctor = null;
    InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);

    assertNotNull(innerClassProperty);
    assertEquals(delegate, innerClassProperty._delegate);
    assertEquals(ctor, innerClassProperty._creator);
  }

  @Test
  public void testInnerClassProperty() {
    InnerClassProperty src = new InnerClassProperty(null, null);
    AnnotatedConstructor ann = null;
    InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);

    assertNotNull(innerClassProperty);
    assertEquals(src, innerClassProperty);
    assertEquals(ann, innerClassProperty._annotated);
    assertEquals((ann == null) ? null : ann.getAnnotated(), innerClassProperty._creator);
  }

  @Test
  public void testInnerClassPropertyWithDeserializer() {
    InnerClassProperty src = new InnerClassProperty(null, null);
    JsonDeserializer<?> deser = null;
    InnerClassProperty innerClassProperty = new InnerClassProperty(src, deser);

    assertNotNull(innerClassProperty);
    assertEquals(src, innerClassProperty);
    assertEquals(src._delegate.withValueDeserializer(deser), innerClassProperty._delegate);
    assertEquals(src._creator, innerClassProperty._creator);
  }

  @Test
  public void testInnerClassPropertyWithNewName() {
    InnerClassProperty src = new InnerClassProperty(null, null);
    PropertyName newName = null;
    InnerClassProperty innerClassProperty = new InnerClassProperty(src, newName);

    assertNotNull(innerClassProperty);
    assertEquals(src, innerClassProperty);
    assertEquals(src._delegate.withName(newName), innerClassProperty._delegate);
    assertEquals(src._creator, innerClassProperty._creator);
  }

  @Test
  public void testWithName() {
    PropertyName newName = null;

    InnerClassProperty result = innerClassProperty.withName(newName);

    assertNotNull(result);
    assertEquals(innerClassProperty, result);
    assertEquals(newName, result.getFullName());
  }

  @Test
  public void testWithValueDeserializer() {
    JsonDeserializer<?> deser = null;

    InnerClassProperty result = innerClassProperty.withValueDeserializer(deser);

    assertNotNull(result);
    assertEquals(innerClassProperty, result);
    assertEquals(innerClassProperty._delegate.withValueDeserializer(deser), result._delegate);
  }

  @Test
  public void testAssignIndex() {
    int index = 0;

    innerClassProperty.assignIndex(index);

    assertEquals(index, innerClassProperty.getPropertyIndex());
  }

  @Test
  public void testGetPropertyIndex() {
    int index = 0;

    innerClassProperty.assignIndex(index);

    assertEquals(index, innerClassProperty.getPropertyIndex());
  }

  @Test
  public void testGetAnnotation() {
    Class<? extends Annotation> acls = null;

    Annotation result = innerClassProperty.getAnnotation(acls);

    assertNull(result);
  }

  @Test
  public void testGetMember() {
    AnnotatedMember result = innerClassProperty.getMember();

    assertNull(result);
  }

  @Test
  public void testDeserializeAndSetWithValueNull() throws IOException {
    JsonParser jp = null;
    DeserializationContext ctxt = null;
    Object bean = new Object();

    innerClassProperty.deserializeAndSet(jp, ctxt, bean);

    assertNull(innerClassProperty.get(bean));
  }

  @Test
  public void testDeserializeAndSetWithValueTypeDeserializer() throws IOException {
    JsonParser jp = null;
    DeserializationContext ctxt = null;
    Object bean = new Object();

    innerClassProperty.deserializeAndSet(jp, ctxt, bean);

    assertNull(innerClassProperty.get(bean));
  }

  @Test
  public void testDeserializeAndSetWithCreator() throws IOException {
    JsonParser jp = null;
    DeserializationContext ctxt = null;
    Object bean = new Object();

    innerClassProperty.deserializeAndSet(jp, ctxt, bean);

    assertNull(innerClassProperty.get(bean));
  }

  @Test
  public void testDeserializeSetAndReturnValue() throws IOException {
    JsonParser jp = null;
    DeserializationContext ctxt = null;
    Object instance = new Object();

    Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);

    assertNull(result);
  }

  @Test
  public void testSet() throws IOException {
    Object instance = new Object();
    Object value = new Object();

    innerClassProperty.set(instance, value);

    assertEquals(value, innerClassProperty.get(instance));
  }

  @Test
  public void testSetAndReturn() throws IOException {
    Object instance = new Object();
    Object value = new Object();

    Object result = innerClassProperty.setAndReturn(instance, value);

    assertEquals(value, result);
    assertEquals(value, innerClassProperty.get(instance));
  }

  @Test
  public void testReadResolve() {
    Object result = innerClassProperty.readResolve();

    assertEquals(innerClassProperty, result);
  }

  @Test
  public void testWriteReplaceWithAnnotated() {
    Object result = innerClassProperty.writeReplace();

    assertEquals(innerClassProperty, result);
  }

  @Test
  public void testWriteReplaceWithoutAnnotated() {
    innerClassProperty = new InnerClassProperty(innerClassProperty, null);

    Object result = innerClassProperty.writeReplace();

    assertEquals(innerClassProperty, result);
  }
}