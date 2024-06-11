import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

public class WritableObjectIdTest {

  @Mock
  private ObjectIdGenerator<Object> generator;
  
  @Mock
  private JsonGenerator gen;
  
  @Mock
  private SerializerProvider provider;
  
  @Mock
  private ObjectIdWriter w;
  
  @Test
  public void testWriteAsId() {
    WritableObjectId writableObjectId = new WritableObjectId(generator);
    
    assertFalse(writableObjectId.writeAsId(gen, provider, w));
    
    Object dummyId = new Object();
    writableObjectId.id = dummyId;
    
    assertFalse(writableObjectId.writeAsId(gen, provider, w));
    
    writableObjectId.idWritten = true;
    assertTrue(writableObjectId.writeAsId(gen, provider, w));
  }
  
  @Test
  public void testGenerateId() {
    WritableObjectId writableObjectId = new WritableObjectId(generator);
    
    Object forPojo = new Object();
    Object generatedId = new Object();
    
    Mockito.when(generator.generateId(forPojo)).thenReturn(generatedId);
    
    assertEquals(generatedId, writableObjectId.generateId(forPojo));
    assertEquals(generatedId, writableObjectId.id);
  }
  
  @Test
  public void testWriteAsField() {
    WritableObjectId writableObjectId = new WritableObjectId(generator);
    
    writableObjectId.id = "testId";
    
    writableObjectId.writeAsField(gen, provider, w);
    assertTrue(writableObjectId.idWritten);
  }
}