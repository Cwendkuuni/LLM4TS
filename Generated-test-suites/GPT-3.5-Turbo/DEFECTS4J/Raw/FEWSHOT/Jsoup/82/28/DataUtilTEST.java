package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFile() {
        // Test loading HTML file
        try {
            Document document = DataUtil.load(new File("test.html"), "UTF-8", "https://www.example.com");
            
            assertNotNull(document);
            assertEquals("https://www.example.com", document.baseUri());
            
            Element body = document.body();
            assertNotNull(body);
            
            Elements paragraphs = body.getElementsByTag("p");
            assertEquals(3, paragraphs.size());
        } catch (IOException e) {
            fail("Exception occurred while loading file: " + e.getMessage());
        }
        
        // Test loading non-existing file
        try {
            Document document = DataUtil.load(new File("non_existing.html"), "UTF-8", "https://www.example.com");
            
            assertNull(document);
        } catch (IOException e) {
            fail("Exception occurred while loading file: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        // Test loading HTML from input stream
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
            
            assertNotNull(document);
            assertEquals("https://www.example.com", document.baseUri());
            
            Element body = document.body();
            assertNotNull(body);
            
            Elements paragraphs = body.getElementsByTag("p");
            assertEquals(3, paragraphs.size());
            
            inputStream.close();
        } catch (IOException e) {
            fail("Exception occurred while loading input stream: " + e.getMessage());
        }
        
        // Test loading empty input stream
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
            
            assertNotNull(document);
            assertEquals("https://www.example.com", document.baseUri());
            
            Element body = document.body();
            assertNull(body);
            
            inputStream.close();
        } catch (IOException e) {
            fail("Exception occurred while loading input stream: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Test loading XML from input stream with XML parser
        try {
            InputStream inputStream = new FileInputStream("test.xml");
            Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.xmlParser());
            
            assertNotNull(document);
            assertEquals("https://www.example.com", document.baseUri());
            
            Element root = document.root();
            assertNotNull(root);
            
            Elements elements = root.getElementsByTag("person");
            assertEquals(2, elements.size());
            
            inputStream.close();
        } catch (IOException e) {
            fail("Exception occurred while loading input stream with parser: " + e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        // Test crossStreams method
        try {
            FileInputStream in = new FileInputStream("input.txt");
            FileOutputStream out = new FileOutputStream("output.txt");
            
            DataUtil.crossStreams(in, out);
            
            in.close();
            out.close();
            
            // Verify contents of output file
            String output = readFile("output.txt");
            
            assertEquals("Hello, World!", output);
        } catch (IOException e) {
            fail("Exception occurred while crossing streams: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        // Test reading input stream to byte buffer
        try {
            InputStream inputStream = new FileInputStream("input.txt");
            
            ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 1024);
            
            assertNotNull(buffer);
            assertEquals(13, buffer.remaining());
            
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String content = new String(bytes, "UTF-8");
            
            assertEquals("Hello, World!", content);
            
            inputStream.close();
        } catch (IOException e) {
            fail("Exception occurred while reading to byte buffer: " + e.getMessage());
        }
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        reader.close();
        return sb.toString();
    }
    
    // Test other methods...
}