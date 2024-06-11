import org.junit.Assert;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = stdKeyDeserializer.deserializeKey("123", ctxt);
        Assert.assertEquals(123, result);
    }

    @Test
    public void testDeserializeKeyWithInvalidKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = stdKeyDeserializer.deserializeKey("NaN", ctxt);
        Assert.assertNotNull(result);
    }

    @Test
    public void testDeserializeKeyWithNullKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = stdKeyDeserializer.deserializeKey(null, ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testGetStringKDForType() throws IOException {
        StdKeyDeserializer.StringKD stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        Assert.assertEquals(String.class, stringKD.getKeyClass());
    }

    @Test
    public void testGetStringKDForTypeWithObject() throws IOException {
        StdKeyDeserializer.StringKD stringKD = StdKeyDeserializer.StringKD.forType(Object.class);
        Assert.assertEquals(Object.class, stringKD.getKeyClass());
    }

    @Test
    public void testDeserializeStringKD() throws IOException {
        StdKeyDeserializer.StringKD stringKD = new StdKeyDeserializer.StringKD(String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = stringKD.deserializeKey("test", ctxt);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testDeserializeDelegatingKD() throws IOException {
        StdKeyDeserializer.StringKD stringKD = new StdKeyDeserializer.StringKD(String.class);
        StdKeyDeserializer.DelegatingKD delegatingKD = new StdKeyDeserializer.DelegatingKD(String.class, stringKD);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = delegatingKD.deserializeKey("test", ctxt);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testEnumKD() throws IOException {
        StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(null, null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = enumKD._parse("VALUE", ctxt);
        Assert.assertNotNull(result);
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        Constructor<?> constructor = String.class.getConstructor(String.class);
        StdKeyDeserializer.StringCtorKeyDeserializer stringCtorKeyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(constructor);
        Object result = stringCtorKeyDeserializer._parse("test", null);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        Method method = String.class.getDeclaredMethod("valueOf", String.class);
        StdKeyDeserializer.StringFactoryKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(method);
        Object result = stringFactoryKeyDeserializer._parse("test", null);
        Assert.assertEquals("test", result);
    }

}