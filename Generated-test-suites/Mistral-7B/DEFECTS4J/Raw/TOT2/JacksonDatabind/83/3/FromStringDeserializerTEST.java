import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = new ObjectMapper.DefaultTypingResolver();
 SimpleModule module = new SimpleModule();
 module.setDefaultTyping(defaultTypingResolver);
 objectMapper = new ObjectMapper(new JsonFactory(), module);
 }

 @Test
 public void testDeserializeFile() throws Exception {
 FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
 File expectedFile = new File("test.txt");
 String fileString = "test.txt";
 File actualFile = fileDeserializer.deserialize(objectMapper.createParser(fileString), objectMapper.getDeserializationContext());
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testDeserializeUrl() throws Exception {
 FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 String urlString = "http://www.google.com";
 URL expectedUrl = new URL(urlString);
 URL actualUrl = urlDeserializer.deserialize(objectMapper.createParser(urlString), objectMapper.getDeserializationContext());
 assertEquals(expectedUrl, actualUrl);
 }

 @Test
 public void testDeserializeUri() throws Exception {
 FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
 String uriString = "http://www.google.com/path";
 URI expectedUri = new URI(uriString);
 URI actualUri = uriDeserializer.deserialize(objectMapper.createParser(uriString), objectMapper.getDeserializationContext());
 assertEquals(expectedUri, actualUri);
 }

 @Test
 public void testDeserializeEmptyString() throws Exception {
 FromStringDeserializer<StringBuilder> stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 String emptyString = "";
 StringBuilder expectedStringBuilder = new StringBuilder();
 StringBuilder actualStringBuilder = stringBuilderDeserializer.deserialize(objectMapper.createParser(emptyString), objectMapper.getDeserializationContext());
 assertEquals(expectedStringBuilder, actualStringBuilder);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidString() throws Exception {
 FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 String invalidLocaleString = "en_US_Invalid";
 Locale expectedLocale = null;
 Locale actualLocale = localeDeserializer.deserialize(objectMapper.createParser(invalidLocaleString), objectMapper.getDeserializationContext());
 assertNull(actualLocale);
 }
}