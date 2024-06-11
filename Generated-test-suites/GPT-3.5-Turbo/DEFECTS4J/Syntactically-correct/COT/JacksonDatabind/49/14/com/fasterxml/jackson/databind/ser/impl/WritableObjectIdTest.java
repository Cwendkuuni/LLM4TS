import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<>());
        JsonGenerator gen = null; //initialize with appropriate JsonGenerator object
        SerializerProvider provider = null; //initialize with appropriate SerializerProvider object
        ObjectIdWriter w = null; //initialize with appropriate ObjectIdWriter object

        boolean result = obj.writeAsId(gen, provider, w);
        //assertions
        
    }

    @Test
    public void testGenerateId() {
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<>());
        Object forPojo = null; //initialize with an object to be used for generating id

        Object result = obj.generateId(forPojo);
        //assertions
        
    }

    @Test
    public void testWriteAsField() {
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<>());
        JsonGenerator gen = null; //initialize with appropriate JsonGenerator object
        SerializerProvider provider = null; //initialize with appropriate SerializerProvider object
        ObjectIdWriter w = null; //initialize with appropriate ObjectIdWriter object

        try {
             obj.writeAsField(gen, provider, w);
             //assertions
        } catch (IOException e) {
             //exception handling assertions
        }
        
    }

}