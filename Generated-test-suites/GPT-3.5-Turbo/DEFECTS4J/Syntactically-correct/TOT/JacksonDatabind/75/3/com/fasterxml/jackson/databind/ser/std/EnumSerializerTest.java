
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EnumSerializerTest {
    
    private static final EnumValues ENUM_VALUES;
    private static final boolean SERIALIZE_AS_INDEX = false;
    
    static {
        List<Enum<?>> enums = new ArrayList<>();
        enums.add(TestEnum.FIRST);
        enums.add(TestEnum.SECOND);
        ENUM_VALUES = EnumValues.constructFromName(null, enums);
    }
    
    // Test Enum
    private enum TestEnum {
        FIRST,
        SECOND
    }
    
    private static final EnumSerializer ENUM_SERIALIZER = new EnumSerializer(ENUM_VALUES, SERIALIZE_AS_INDEX);
    
    @Test
    public void testGetEnumValues() {
        assertEquals(ENUM_VALUES, ENUM_SERIALIZER.getEnumValues());
    }
    
    @Test
    public void testSerializeAsIndex() {
        SerializerProvider provider = mock(SerializerProvider.class);
        assertEquals(SERIALIZE_AS_INDEX, ENUM_SERIALIZER._serializeAsIndex(provider));
    }
    
    @Test
    public void testSerialize() {
        JsonSerializer<String> stringSerializer = mock(JsonSerializer.class);
        JsonSerializer<Integer> integerSerializer = mock(JsonSerializer.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        Enum<TestEnum> testEnum = TestEnum.FIRST;
        
        try {
            // Should write enum value as a string
            ENUM_SERIALIZER.serialize(testEnum, generator, provider);
            // TODO: Verify output in JSON generator
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            // Should write enum value as an index
            ENUM_SERIALIZER.serialize(testEnum, generator, provider);
            // TODO: Verify output in JSON generator
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        
        ArrayNode expectedArrayNode = new ArrayNode(null);
        expectedArrayNode.add("FIRST");
        expectedArrayNode.add("SECOND");
        
        ObjectNode expectedObjectNode = new ObjectNode(null);
        expectedObjectNode.putArray("enum").addAll(expectedArrayNode);
        
        assertEquals(expectedArrayNode, ENUM_SERIALIZER.getSchema(provider, typeHint));
        
        // TODO: Test for index serialization
        
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        
        try {
            ENUM_SERIALIZER.acceptJsonFormatVisitor(visitor, typeHint);
            // TODO: Verify visitor calls
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Enable WRITE_ENUMS_USING_TO_STRING for this test case
        //serializers.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        
        try {
            ENUM_SERIALIZER.acceptJsonFormatVisitor(visitor, typeHint);
            // TODO: Verify visitor calls
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedSerializationShape() {
        Class<TestEnum> enumClass = TestEnum.class;
        JsonFormat.Value format = new JsonFormat.Value();
        ENUM_SERIALIZER._isShapeWrittenUsingIndex(enumClass, format, true);
    }
}
