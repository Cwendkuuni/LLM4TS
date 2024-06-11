import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class LookupTranslatorTest {
  
  @Test
  public void testLookupTranslator() {
    LookupTranslator translator = new LookupTranslator();
    assertNotNull(translator);
  }
  
  @Test
  public void testTranslate() throws IOException {
    CharSequence[] seq1 = {"abc", "xyz"};
    CharSequence[] seq2 = {"def", "uvw"};
    CharSequence[][] lookup = {seq1, seq2};
    LookupTranslator translator = new LookupTranslator(lookup);
    
    CharSequence input = "abc";
    StringBuilder output = new StringBuilder();
    Writer writer = new Writer() {
      @Override
      public void write(char[] cbuf, int off, int len) throws IOException {
        output.append(cbuf, off, len);
      }
      
      @Override
      public void flush() throws IOException {
      }
      
      @Override
      public void close() throws IOException {
      }
    };
    
    int index = 0;
    int result = translator.translate(input, index, writer);
    
    assertEquals(3, result);
    assertEquals("xyz", output.toString());
  }
  
  @Test
  public void testTranslate_noMatch() throws IOException {
    CharSequence[] seq1 = {"abc", "xyz"};
    CharSequence[] seq2 = {"def", "uvw"};
    CharSequence[][] lookup = {seq1, seq2};
    LookupTranslator translator = new LookupTranslator(lookup);
    
    CharSequence input = "123";
    StringBuilder output = new StringBuilder();
    Writer writer = new Writer() {
      @Override
      public void write(char[] cbuf, int off, int len) throws IOException {
        output.append(cbuf, off, len);
      }
      
      @Override
      public void flush() throws IOException {
      }
      
      @Override
      public void close() throws IOException {
      }
    };
    
    int index = 0;
    int result = translator.translate(input, index, writer);
    
    assertEquals(0, result);
    assertEquals("", output.toString());
  }
}