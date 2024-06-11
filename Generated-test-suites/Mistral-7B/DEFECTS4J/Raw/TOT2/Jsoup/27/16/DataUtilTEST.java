package org.junit.tests;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

@Test
public void testLoadFile() throws IOException {
File file = new File("path/to/your/testfile.html");
String charsetName = "UTF-8";
String baseUri = "http://www.example.com";
Document doc = DataUtil.load(file, charsetName, baseUri);
assertNotNull(doc);
}

@Test
public void testLoadNonExistentFile() throws IOException {
File file = new File("path/to/non-existent-file.html");
String charsetName = "UTF-8";
String baseUri = "http://www.example.com";
Document doc = assertThrows(FileNotFoundException.class, () -> DataUtil.load(file, charsetName, baseUri));
assertNull(doc);
}

@Test
public void testLoadInvalidUTF8File() throws IOException {
File file = new File("path/to/your/testfile.html"); // replace with a file with invalid UTF-8 encoding
String charsetName = "UTF-8";
String baseUri = "http://www.example.com";
Document doc = assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
assertNull(doc);
}

@Test
public void testLoadEmptyFile() throws IOException {
File file = new File("path/to/your/empty-file.html");
String charsetName = "UTF-8";
String baseUri = "http://www.example.com";
Document doc = DataUtil.load(file, charsetName, baseUri);
assertNotNull(doc);
assertTrue(doc.bodyElements().isEmpty());
}

@Test
public void testLoadNullCharsetName() throws IOException {
File file = new File("path/to/your/testfile.html");
String charsetName = null;
String baseUri = "http://www.example.com";
Document doc = assertThrows(NullPointerException.class, () -> DataUtil.load(file, charsetName, baseUri));
assertNull(doc);
}

@Test
public void testLoadNullBaseUri() throws IOException {
File file = new File("path/to/your/testfile.html");
String charsetName = "UTF-8";
String baseUri = null;
Document doc = assertThrows(NullPointerException.class, () -> DataUtil.load(file, charsetName, baseUri));
assertNull(doc);
}
}