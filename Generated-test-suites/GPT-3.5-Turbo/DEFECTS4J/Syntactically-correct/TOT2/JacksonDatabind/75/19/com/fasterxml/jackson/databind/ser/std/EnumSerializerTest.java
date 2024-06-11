import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        // Test serialization with index
        EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), true);
        MyEnum enumValue = MyEnum.VALUE1;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        
        serializer.serialize(enumValue, gen, provider);
        gen.close();
        
        // Add assertions here
    }

    @Test
    public void testContextual() {
        // Test contextual method with property
        EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), true);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        BeanProperty property = new BeanProperty.Impl();
        
        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, property);
        
        // Add assertions here
    }

    // Add more test cases for other public methods as needed

}