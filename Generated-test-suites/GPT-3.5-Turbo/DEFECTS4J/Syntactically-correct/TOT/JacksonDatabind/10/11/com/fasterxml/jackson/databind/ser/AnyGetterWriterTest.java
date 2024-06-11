
package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private BeanProperty property;
    private AnnotatedMember accessor;
    private MapSerializer serializer;

    @Before
    public void setUp() {
        property = new FakeBeanProperty();
        accessor = new FakeAnnotatedMember();
        serializer = new FakeMapSerializer();
        anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
    }

    @Test
    public void testGetAndSerializeWithNullBean() {
        Object bean = null;
        JsonGenerator generator = new FakeJsonGenerator();
        SerializerProvider provider = new FakeSerializerProvider();

        try {
            anyGetterWriter.getAndSerialize(bean, generator, provider);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetAndSerializeWithNullValue() {
        FakeBean bean = new FakeBean(null);
        JsonGenerator generator = new FakeJsonGenerator();
        SerializerProvider provider = new FakeSerializerProvider();

        try {
            anyGetterWriter.getAndSerialize(bean, generator, provider);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetAndSerializeWithNonMapValue() {
        FakeBean bean = new FakeBean("nonMapValue");
        JsonGenerator generator = new FakeJsonGenerator();
        SerializerProvider provider = new FakeSerializerProvider();

        try {
            anyGetterWriter.getAndSerialize(bean, generator, provider);
            fail("Expected JsonMappingException but got no exception");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (null) not java.util.Map but java.lang.String", e.getMessage());
        } catch (Exception e) {
            fail("Expected JsonMappingException but got " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testGetAndSerializeWithMapValueUsingMapSerializer() {
        Map<String, String> mapValue = new HashMap<>();
        mapValue.put("key", "value");
        FakeBean bean = new FakeBean(mapValue);
        FakeJsonGenerator generator = new FakeJsonGenerator();
        FakeSerializerProvider provider = new FakeSerializerProvider();

        try {
            anyGetterWriter.getAndSerialize(bean, generator, provider);
            assertTrue(generator.serializeFieldsCalled);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private static class FakeBeanProperty extends BeanProperty {
        @Override
        public String getFullName() {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }
    }

    private static class FakeAnnotatedMember extends AnnotatedMember {
        @Override
        public Class<?> getRawType() {
            return null;
        }

        @Override
        public boolean hasGetter() {
            return false;
        }

        @Override
        public boolean hasSetter() {
            return false;
        }

        @Override
        public String getName() {
            return "null";
        }
    }

    private static class FakeMapSerializer extends MapSerializer {
        public FakeMapSerializer() {
            super(null, null, null, false, null, null, null);
        }
    }

    private static class FakeBean {
        private Object value;

        public FakeBean(Object value) {
            this.value = value;
        }

        public Object getAnyGetter() {
            return this.value;
        }
    }

    private static class FakeJsonGenerator extends JsonGenerator {
        public boolean serializeFieldsCalled = false;

        @Override
        public void serializeFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) {
            serializeFieldsCalled = true;
        }
    }

    private static class FakeSerializerProvider extends DefaultSerializerProvider {
        @Override
        public MapSerializer handlePrimaryContextualization(MapSerializer serializer, BeanProperty prop) {
            return serializer;
        }
    }
}

