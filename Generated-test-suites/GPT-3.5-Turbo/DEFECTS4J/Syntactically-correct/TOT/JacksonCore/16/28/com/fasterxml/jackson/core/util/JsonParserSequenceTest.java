import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;


public class JsonParserSequenceTest {

    @Test
    public void testClose() {
        //Arrange
        JsonParserSequence jp = new JsonParserSequence(new JsonParser[] {new JsonParser() {
            @Override
            public void close() {
                // Test that this method is called
                fail("close() method is not supposed to be called in this test");
            }
        }, new JsonParser() {
            @Override
            public void close() {
                // Test that this method is called
                fail("close() method is not supposed to be called in this test");
            }
        }});
        
        //Act
        try {
            jp.close();
        } catch (Exception e) {
            //Assert
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testNextToken() {
        //Arrange
        JsonParserSequence jp = new JsonParserSequence(new JsonParser[] {new JsonParser() {
            @Override
            public JsonToken nextToken() throws JsonParseException {
                //Assert
                fail("nextToken() method is not supposed to be called in this test");
                return null;
            }
        }, new JsonParser() {
            private int called = 0;
            
            @Override
            public JsonToken nextToken() throws JsonParseException {
                if (called == 0) {
                    // Assert that nextToken() method is called once
                    called++;
                    return JsonToken.START_OBJECT;
                } else {
                    // Assert that nextToken() method is called twice
                    called++;
                    return JsonToken.END_OBJECT;
                }
            }
        }});
        
        //Act
        try {
            JsonToken token = jp.nextToken();
            
            //Assert
            assertEquals(JsonToken.START_OBJECT, token);
            
            token = jp.nextToken();
            assertEquals(JsonToken.END_OBJECT, token);
        } catch (Exception e) {
            //Assert
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testContainedParsersCount() {
        //Arrange
        JsonParserSequence jp = new JsonParserSequence(new JsonParser[] {new JsonParser(), new JsonParser(), new JsonParser()});
        
        //Act
        int count = jp.containedParsersCount();
        
        //Assert
        assertEquals(3, count);
    }
}