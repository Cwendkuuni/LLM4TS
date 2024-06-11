import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenTrueAndAlwaysAsIdTrue_shouldReturnTrue() throws IOException {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        w.alwaysAsId = true;

        // Act
        boolean result = writableObjectId.writeAsId(gen, provider, w);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(gen).writeObjectRef("123");
    }
    
    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenFalseAndAlwaysAsIdTrue_shouldReturnTrue() throws IOException {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        w.alwaysAsId = true;

        // Act
        boolean result = writableObjectId.writeAsId(gen, provider, w);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(gen).writeObjectRef("123");
    }
    
    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenFalseAndAlwaysAsIdFalse_shouldReturnFalse() throws IOException {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        w.alwaysAsId = false;

        // Act
        boolean result = writableObjectId.writeAsId(gen, provider, w);

        // Assert
        Assert.assertFalse(result);
        Mockito.verify(gen, Mockito.never()).writeObjectRef(Mockito.anyString());
        Mockito.verify(w, Mockito.never()).serializer.serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testWriteAsId_withNullId_shouldReturnFalse() throws IOException {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        w.alwaysAsId = true;

        // Act
        boolean result = writableObjectId.writeAsId(gen, provider, w);

        // Assert
        Assert.assertFalse(result);
        Mockito.verify(gen, Mockito.never()).writeObjectRef(Mockito.anyString());
        Mockito.verify(w, Mockito.never()).serializer.serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenTrueAndCanWriteObjectIdTrue_shouldReturnTrue()
            throws IOException {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Mockito.when(gen.canWriteObjectId()).thenReturn(true);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        w.alwaysAsId = true;

        // Act
        boolean result = writableObjectId.writeAsId(gen, provider, w);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(gen).writeObjectRef("123");
        Mockito.verify(gen, Mockito.never()).writeFieldName(Mockito.anyString());
        Mockito.verify(w, Mockito.never()).serializer.serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenTrueAndCanWriteObjectIdFalse_shouldReturnTrue()
            throws IOException {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Mockito.when(gen.canWriteObjectId()).thenReturn(false);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        w.alwaysAsId = true;

        // Act
        boolean result = writableObjectId.writeAsId(gen, provider, w);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(gen, Mockito.never()).writeObjectRef(Mockito.anyString());
        Mockito.verify(w).serializer.serialize(writableObjectId.id, gen, provider);
    }

    @Test
    public void testGenerateId_shouldGenerateIdAndUpdateIdProperty() {
        // Arrange
        ObjectIdGenerator<?> generator = Mockito.mock(ObjectIdGenerator.class);
        Object forPojo = new Object();
        Mockito.when(generator.generateId(forPojo)).thenReturn("123");
        
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        // Act
        Object result = writableObjectId.generateId(forPojo);

        // Assert
        Assert.assertEquals("123", result);
        Assert.assertEquals("123", writableObjectId.id);
    }
    
    @Test
    public void testWriteAsField_withIdWrittenFalseAndCanWriteObjectIdTrue_shouldWriteObjectId() throws IOException {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Mockito.when(gen.canWriteObjectId()).thenReturn(true);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(w.propertyName).thenReturn(Mockito.mock(SerializableString.class));
        
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.idWritten = false;

        // Act
        writableObjectId.writeAsField(gen, provider, w);

        // Assert
        Mockito.verify(gen).writeObjectId(Mockito.anyString());
        Mockito.verify(w, Mockito.never()).serializer.serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testWriteAsField_withIdWrittenFalseAndCanWriteObjectIdFalse_shouldWriteFieldNameAndSerializeId() throws IOException {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Mockito.when(gen.canWriteObjectId()).thenReturn(false);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        SerializableString name = Mockito.mock(SerializableString.class);
        Mockito.when(w.propertyName).thenReturn(name);
        
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.idWritten = false;

        // Act
        writableObjectId.writeAsField(gen, provider, w);

        // Assert
        Mockito.verify(gen).writeFieldName(name);
        Mockito.verify(w).serializer.serialize(writableObjectId.id, gen, provider);
    }
    
    @Test
    public void testWriteAsField_withIdWrittenTrue_shouldNotWriteFieldNameAndSerializeId() throws IOException {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        SerializableString name = Mockito.mock(SerializableString.class);
        Mockito.when(w.propertyName).thenReturn(name);
        
        WritableObjectId writableObjectId = new WritableObjectId(Mockito.mock(ObjectIdGenerator.class));
        writableObjectId.idWritten = true;

        // Act
        writableObjectId.writeAsField(gen, provider, w);

        // Assert
        Mockito.verify(gen, Mockito.never()).writeFieldName(Mockito.anyString());
        Mockito.verify(w).serializer.serialize(writableObjectId.id, gen, provider);
    }

}