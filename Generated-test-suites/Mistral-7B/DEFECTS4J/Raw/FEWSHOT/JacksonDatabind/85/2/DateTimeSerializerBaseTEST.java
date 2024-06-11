package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @JsonSerialize(using = DateTimeSerializerBase.class)
    static class TestBean {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = " yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @JsonProperty("empty")
        private String empty;
    }

    @Test
    public void testCreateContextual() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        TestBean bean = new TestBean();
        SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();

        BeanProperty property = new AnnotatedClass(TestBean.class).findProperty("date");

        JsonSerializer<TestBean> serializer = serializers.findValueSerializer(TestBean.class);

        JsonFormat.Value format = JsonFormat.value().shape(JsonFormat.Shape.STRING)
                .pattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").locale(Locale.US).timeZone(TimeZone.UTC);

        JsonSerializer<TestBean> formattedSerializer = serializer.withContextualHandler(new JsonFormat.Value(format));

        assertNotNull(formattedSerializer);

        JsonParser parser = mapper.getFactory().createParser(new Object() {}.getClass().getDeclaredConstructor().newInstance());
        JsonNode tree = serializers.findValueSerializer(TestBean.class).serializeValue(parser, bean, null);
        mapper.writeValueAsString(tree);

        JsonSerializer<TestBean> serializedSerializer = serializers.findValueSerializer(TestBean.class);
        JsonSerializer<TestBean> serializedFormattedSerializer = serializedSerializer.withContextualHandler(formattedSerializer);

        assertNotNull(serializedFormattedSerializer);

        TestBean deserialized = mapper.readValue(mapper.writeValueAsString(bean), TestBean.class);
        assertNotNull(deserialized);
        assertEquals(bean.getDate(), deserialized.getDate());
    }

    @Test
    public void testIsEmpty() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TestBean bean = new TestBean();
        SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();

        assertEquals(true, DateTimeSerializerBase.class.newInstance().isEmpty(null));
        assertEquals(true, DateTimeSerializerBase.class.newInstance().isEmpty(serializers, null));

        bean.setDate(null);
        assertEquals(true, DateTimeSerializerBase.class.newInstance().isEmpty(bean));
        assertEquals(true, DateTimeSerializerBase.class.newInstance().isEmpty(serializers, bean));

        bean.setDate(new Date(0L));
        assertEquals(true, DateTimeSerializerBase.class.newInstance().isEmpty(bean));
        assertEquals(true, DateTimeSerializerBase.class.newInstance().isEmpty(serializers, bean));

        bean.setDate(new Date());
        assertEquals(false, DateTimeSerializerBase.class.newInstance().isEmpty(bean));
        assertEquals(false, DateTimeSerializerBase.class.newInstance().isEmpty(serializers, bean));
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        TestBean bean = new TestBean();
        bean.setDate(new Date());

        SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();

        JsonSerializer<TestBean> serializer = serializers.findValueSerializer(TestBean.class);
        JsonSerializer<TestBean> formattedSerializer = serializer.withContextualHandler(null);

        JsonGenerator gen = serializers.getGeneratorFactory().createGenerator(new Object() {}.getClass(), new StringWriter());
        serializer.serialize(bean, gen, serializers);
        gen.flush();
        String serialized = gen.getOutputStream().toString();

        JsonParser parser = mapper.getFactory().createParser(new Object() {}.getClass().getDeclaredConstructor().newInstance());
        JsonNode tree = serializers.findValueSerializer(TestBean.class).deserialize(parser, null);

        assertEquals(serialized, mapper.writeValueAsString(bean));

        formattedSerializer = formattedSerializer.withContextualHandler(JsonFormat.value().shape(JsonFormat.Shape.STRING)
                .pattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").locale(Locale.US).timeZone(TimeZone.UTC));

        gen = serializers.getGeneratorFactory().createGenerator(new Object() {}.getClass(), new StringWriter());
        serializer.serialize(bean, gen, serializers);
        gen.setFormat(formattedSerializer.getFormat());
        gen.flush();
        serialized = gen.getOutputStream().toString();

        assertEquals(serialized, mapper.writeValueAsString(bean) + "Z");
    }
}