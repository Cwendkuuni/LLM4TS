import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.Node;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SourceMapTest {

  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
    Node node = Node.newText("test");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String expectedSourceMap = "{\n" +
            "  \"version\": 3,\n" +
            "  \"file\": \"test.js\",\n" +
            "  \"sources\": [\n" +
            "    \"test.js\"\n" +
            "  ],\n" +
            "  \"sourcesContent\": [\n" +
            "    \"text: test\\n\"\n" +
            "  ],\n" +
            "  \"name\": \"test\",\n" +
            "  \"mappings\": [\n" +
            "    {\n" +
            "      \"original\": {\n" +
            "        \"source\": \"test.js\",\n" +
            "        \"line\": 1,\n" +
            "        \"column\": 1\n" +
            "      },\n" +
            "      \"generated\": {\n" +
            "        \"line\": 1,\n" +
            "        \"column\": 1\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

    String actualSourceMap = "";
    try (Appendable out = new StringBuilder()) {
      sourceMap.appendTo(out, "test.js");
      actualSourceMap = out.toString();
    }

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  // Add more test cases for other methods, check edge cases and exceptions.

  // ...

  
  // Your complete test file should start here
  
}