import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {
  
  @Test
  public void testAppendMethod() {
    CodeConsumer consumer = new CodeConsumer();
    String testString = "TestString";
    consumer.append(testString);
    assertEquals(testString, consumer.getLastChar());
  }
}