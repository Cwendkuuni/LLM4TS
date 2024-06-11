import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_file() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
      String filePath = "test/test.txt";
      File expected = new File(filePath);
      File result = deserializer._deserialize(filePath, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_URL() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
      String urlString = "http://www.example.com";
      URL expected = new URL(urlString);
      URL result = deserializer._deserialize(urlString, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_URI() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
      String uriString = "http://www.example.com";
      URI expected = URI.create(uriString);
      URI result = deserializer._deserialize(uriString, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Class() throws IOException, ClassNotFoundException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
      String className = "java.lang.String";
      Class<?> expected = Class.forName(className);
      Class<?> result = deserializer._deserialize(className, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
      // Not able to test as JavaType is an abstract class
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
      String currencyCode = "USD";
      Currency expected = Currency.getInstance(currencyCode);
      Currency result = deserializer._deserialize(currencyCode, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
      String pattern = "\\d+";
      Pattern expected = Pattern.compile(pattern);
      Pattern result = deserializer._deserialize(pattern, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
      String localeString = "en";
      Locale expected = new Locale(localeString);
      Locale result = deserializer._deserialize(localeString, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
      String charsetName = "UTF-8";
      Charset expected = Charset.forName(charsetName);
      Charset result = deserializer._deserialize(charsetName, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
      String timeZoneId = "America/Los_Angeles";
      TimeZone expected = TimeZone.getTimeZone(timeZoneId);
      TimeZone result = deserializer._deserialize(timeZoneId, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
      String hostAddress = "192.168.1.1";
      InetAddress expected = InetAddress.getByName(hostAddress);
      InetAddress result = deserializer._deserialize(hostAddress, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
      String inetSocketAddressString = "192.168.1.1:8080";
      InetSocketAddress expected = new InetSocketAddress("192.168.1.1", 8080);
      InetSocketAddress result = deserializer._deserialize(inetSocketAddressString, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_unknownType() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
      String value = "value";
      assertThrows(IllegalArgumentException.class, () -> {
        deserializer._deserialize(value, null);
      });
    }

    @Test
    public void testDeserialize_emptyStringForURI() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
      String uriString = "";
      URI expected = URI.create("");
      URI result = deserializer._deserialize(uriString, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_emptyStringForLocale() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
      String localeString = "";
      Locale expected = new Locale("");
      Locale result = deserializer._deserialize(localeString, null);
      assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_emptyStringForChameleon() throws IOException {
      FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
      String value = "";
      assertNull(deserializer._deserialize(value, null));
    }

}