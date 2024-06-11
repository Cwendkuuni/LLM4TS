import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {
  
  @Test
  public void testDeserialize_File() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
    File expected = new File("test.json");
    File actual = deserializer._deserialize("test.json", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_URL() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
    URL expected = new URL("http://example.com");
    URL actual = deserializer._deserialize("http://example.com", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_URI() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
    URI expected = URI.create("http://example.com");
    URI actual = deserializer._deserialize("http://example.com", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_Class() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
    Class<?> expected = String.class;
    Class<?> actual = deserializer._deserialize("java.lang.String", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_JavaType() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
    JavaType expected = new JavaType(String.class);
    JavaType actual = deserializer._deserialize("java.lang.String", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_Currency() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
    Currency expected = Currency.getInstance("USD");
    Currency actual = deserializer._deserialize("USD", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_Pattern() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
    Pattern expected = Pattern.compile("\\d+");
    Pattern actual = deserializer._deserialize("\\d+", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_Locale() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
    Locale expected = new Locale("en", "US");
    Locale actual = deserializer._deserialize("en_US", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_Charset() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
    Charset expected = Charset.forName("UTF-8");
    Charset actual = deserializer._deserialize("UTF-8", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_TimeZone() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
    TimeZone expected = TimeZone.getTimeZone("GMT");
    TimeZone actual = deserializer._deserialize("GMT", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_InetAddress() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
    InetAddress expected = InetAddress.getByName("localhost");
    InetAddress actual = deserializer._deserialize("localhost", null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserialize_InetSocketAddress() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
    InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
    InetSocketAddress actual = deserializer._deserialize("localhost:8080", null);
    assertEquals(expected, actual);
  }
}