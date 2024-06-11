package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled() throws JsonProcessingException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testGetFeatureMask() throws JsonProcessingException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertEquals(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws JsonProcessingException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        PrettyPrinter prettyPrinter = generator.useDefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        assertTrue(prettyPrinter instanceof DefaultPrettyPrinter);
    }

    @Test
    public void testSetCodec() throws JsonProcessingException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getEncoder();
        generator.setCodec(objectCodec);
        assertEquals(objectCodec, generator.getCodec());
    }

    @Test
    public void testGetOutputContext() throws JsonProcessingException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        JsonWriteContext outputContext = generator.getOutputContext();
        assertNotNull(outputContext);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteObjectNullWithCodecNull() throws JsonProcessingException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        generator.writeObject(null);
    }

    @Test
    public void testWriteObject() throws JsonProcessingException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        String expectedJson = "{\"name\":\"John Doe\",\"age\":30,\"isStudent\":false}";
        String json = generator.writeObjectToString(new Person("John Doe", 30, false));
        assertEquals(expectedJson, json);
    }

    @Test
    public void testWriteTree() throws JsonProcessingException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        TreeNode treeNode = new ObjectMapper().readTree("{\"name\":\"John Doe\",\"age\":30,\"isStudent\":false}");
        String json = generator.writeTreeToString(treeNode);
        assertEquals(expectedJson, json);
    }

    @Test
    public void testFlush() throws JsonProcessingException, IOException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generator.setOutput(outputStream);
        generator.writeStartObject();
        generator.writeStringField("name", "John Doe");
        generator.writeNumberField("age", 30);
        generator.writeBooleanField("isStudent", false);
        generator.writeEndObject();
        generator.flush();

        String json = new String(outputStream.toByteArray());
        String expectedJson = "{\"name\":\"John Doe\",\"age\":30,\"isStudent\":false}";
        assertEquals(expectedJson, json);
    }

    @Test
    public void testClose() throws JsonProcessingException, IOException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generator.setOutput(outputStream);
        generator.writeStartObject();
        generator.writeStringField("name", "John Doe");
        generator.writeNumberField("age", 30);
        generator.writeBooleanField("isStudent", false);
        generator.writeEndObject();
        generator.close();
        String json = new String(outputStream.toByteArray());
        String expectedJson = "{\"name\":\"John Doe\",\"age\":30,\"isStudent\":false}";
        assertEquals(expectedJson, json);
    }

    @Test
    public void testIsClosed() throws JsonProcessingException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertFalse(generator.isClosed());

        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test
    public void testVersion() throws JsonProcessingException {
        GeneratorBase generator = new TestGeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertEquals(new Version("2.12.0"), generator.version());
    }

    private static class Person {
        private final String name;
        private final int age;
        private final boolean isStudent;

        public Person(String name, int age, boolean isStudent) {
            this.name = name;
            this.age = age;
            this.isStudent = isStudent;
        }
    }
}

class TestGeneratorBase extends GeneratorBase {

    public TestGeneratorBase(int features, ObjectCodec codec) {
        super(features, codec, null);
    }

    public TestGeneratorBase(int features, ObjectCodec codec, JsonWriteContext ctxt) {
        super(features, codec, ctxt);
    }

    @Override
    protected PrettyPrinter _constructDefaultPrettyPrinter() {
        return new TestDefaultPrettyPrinter();
    }

    @Override
    protected void _releaseBuffers() {
        // Empty implementation
    }

    @Override
    protected void _verifyValueWrite(String typeMsg) throws IOException {
        // Empty implementation
    }

    @Override
    public String writeObjectToString(Object value) throws JsonProcessingException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.setOutput(outputStream);
        this.writeObject(value);
        this.flush();
        return new String(outputStream.toByteArray());
    }
}

class TestDefaultPrettyPrinter extends DefaultPrettyPrinter {

    @Override
    public int getFeatureMask() {
        return JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT.getMask();
    }
}