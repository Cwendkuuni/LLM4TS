import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {
	
	@Test
	public void testConstructor() {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        assertNotNull(new ObjectIdReferenceProperty(forward, objectIdInfo));
	}

	@Test
	public void testConstructorWithDeserializerAndNullValueProvider() {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        assertNotNull(new ObjectIdReferenceProperty(forward, objectIdInfo, deser, nva));
	}

	@Test
	public void testConstructorWithPropertyName() {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        PropertyName newName = new PropertyName("name");
        assertNotNull(new ObjectIdReferenceProperty(forward, objectIdInfo, newName));
	}

	@Test
	public void testWithName() {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        PropertyName newName = new PropertyName("name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(property.withName(newName));
	}

	@Test
	public void testWithValueDeserializer() {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(property.withValueDeserializer(deser));
	}

	@Test
	public void testWithNullProvider() {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(property.withNullProvider(nva));
	}

	@Test
	public void testFixAccess() {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.fixAccess(new DeserializationConfig());
	}

	@Test
	public void testGetAnnotation() {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNull(property.getAnnotation(Annotation.class));
	}

	@Test
	public void testGetMember() {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNull(property.getMember());
	}

	@Test
	public void testGetCreatorIndex() {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(0, property.getCreatorIndex());
	}

	@Test
	public void testDeserializeAndSet() throws IOException {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);
	}

	@Test
	public void testDeserializeSetAndReturn() throws IOException {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
	}

	@Test
	public void testSet() throws IOException {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
	}

	@Test
	public void testSetAndReturn() throws IOException {
		SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();
        assertNotNull(property.setAndReturn(instance, value));
	}

	@Test
	public void testPropertyReferring() {
		ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        assertNotNull(new PropertyReferring(parent, ref, type, ob));
	}

	@Test
	public void testHandleResolvedForwardReference() throws IOException {
		ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        PropertyReferring propertyReferring = new PropertyReferring(parent, ref, type, ob);
        propertyReferring.handleResolvedForwardReference(new Object(), new Object());
	}

}