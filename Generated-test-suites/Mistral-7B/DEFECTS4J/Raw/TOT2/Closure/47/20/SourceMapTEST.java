import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeFactory;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.debugging.sourcemap.FilePosition;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {
  // ... (previous test case)

  @Test
  public void testAddMapping_NullOutputStartPosition() {
    FilePosition outputStartPosition = null;

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    verify(sourceMap.generator, never()).addMapping(
        anyString(), anyString(),
        argThat(not(isInstanceOf(FilePosition.class))),
        argThat(instanceOf(FilePosition.class)));
  }

  @Test
  public void testAddMapping_NullOutputEndPosition() {
    FilePosition outputEndPosition = null;

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    verify(sourceMap.generator, never()).addMapping(
        anyString(), anyString(),
        argThat(instanceOf(FilePosition.class)),
        argThat(not(isInstanceOf(FilePosition.class))));
  }

  @Test
  public void testAddMapping_DifferentPrefixes() {
    String sourceFile1 = "/path/to/file1.js";
    String sourceFile2 = "/path/to/file2.js";
    String prefix1 = "prefix1";
    String prefix2 = "prefix2";

    when(outputStartPosition.getSourceFileName()).thenReturn(sourceFile1);
    when(outputEndPosition.getSourceFileName()).thenReturn(sourceFile2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    verify(sourceMap.generator, times(1)).addMapping(
        eq("/path/to/file1.js"), eq(""),
        argThat(instanceOf(FilePosition.class)),
        argThat(instanceOf(FilePosition.class)));
    verify(sourceMap.generator, times(1)).addMapping(
        eq("/path/to/file2.js"), eq(""),
        argThat(instanceOf(FilePosition.class)),
        argThat(instanceOf(FilePosition.class)));

    verify(sourceMap).setPrefixMappings(ImmutableList.of(
        new SourceMap.LocationMapping(prefix1, prefix2)));
  }

  @Test
  public void testAppendTo_NullOutputStream() {
    Appendable outputStream = null;

    sourceMap.appendTo(outputStream, "name");

    assertFalse("Method should not call appendTo with null output stream",
        sourceMap.generator.isAppending());
  }

  @Test(expected = java.io.IOException.class)
  public void testAppendTo_IOException() throws IOException {
    doThrow(new IOException("IOException")).when(outputStream).append(anyString());

    sourceMap.appendTo(outputStream, "name");
  }

  // ... (other test cases)

  @After
  public void tearDown() {
    reset(outputStream, errorReporter, sourceMap, sourceMap.generator);
  }
}