
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.common.collect.ImmutableMap;

public class FileContentsTest {
  
  private FileContents fileContents;
    
  @Before
  public void setUp() {
    String[] lines = {"This is line 1",
                      "// This is a single line comment",
                      "This is line 3",
                      "/* This is a block comment",
                      "   spanning multiple lines */",
                      "This is line 6"};
    fileContents = new FileContents("sample.txt", lines);
  }
  
  @Test
  public void testGetCppComments() {
    ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
    
    assertEquals(1, cppComments.size());
    assertTrue(cppComments.containsKey(2));
    assertEquals("// This is a single line comment", cppComments.get(2).getText());
  }
  
  @Test
  public void testGetCComments() {
    ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
    
    assertEquals(1, cComments.size());
    assertTrue(cComments.containsKey(4));
    assertEquals(1, cComments.get(4).size());
    assertEquals("/* This is a block comment\n   spanning multiple lines */", cComments.get(4).get(0).getText());
  }
  
  @Test
  public void testGetJavadocBefore() {
    TextBlock javadocComment = fileContents.getJavadocBefore(6);
    
    assertNotNull(javadocComment);
    assertEquals("/* This is a block comment\n   spanning multiple lines */", javadocComment.getText());
  }
  
  @Test
  public void testGetText() {
    FileText text = fileContents.getText();
    
    assertNotNull(text);
    assertEquals("This is line 1\n// This is a single line comment\nThis is line 3\n/* This is a block comment\n   spanning multiple lines */\nThis is line 6", 
                     text.toString());
  }
  
  @Test
  public void testGetLines() {
    String[] lines = fileContents.getLines();
    
    assertNotNull(lines);
    assertEquals(6, lines.length);
    assertEquals("This is line 1", lines[0]);
    assertEquals("// This is a single line comment", lines[1]);
    assertEquals("This is line 3", lines[2]);
    assertEquals("/* This is a block comment", lines[3]);
    assertEquals("   spanning multiple lines */", lines[4]);
    assertEquals("This is line 6", lines[5]);
  }
  
  @Test
  public void testGetLine() {
    String line = fileContents.getLine(3);
    
    assertEquals("This is line 3", line);
  }
  
  @Test
  public void testGetFileName() {
    String fileName = fileContents.getFileName();
    
    assertEquals("sample.txt", fileName);
  }
  
  @Test
  public void testLineIsBlank() {
    assertTrue(fileContents.lineIsBlank(1));
    assertFalse(fileContents.lineIsBlank(2));
    assertFalse(fileContents.lineIsBlank(3));
    assertFalse(fileContents.lineIsBlank(4));
    assertFalse(fileContents.lineIsBlank(5));
    assertFalse(fileContents.lineIsBlank(6));
  }
  
  @Test
  public void testLineIsComment() {
    assertFalse(fileContents.lineIsComment(1));
    assertTrue(fileContents.lineIsComment(2));
    assertFalse(fileContents.lineIsComment(3));
    assertTrue(fileContents.lineIsComment(4));
    assertFalse(fileContents.lineIsComment(5));
    assertFalse(fileContents.lineIsComment(6));
  }
  
  @Test
  public void testHasIntersectionWithComment() {
    assertFalse(fileContents.hasIntersectionWithComment(1, 1, 1, 10));
    assertTrue(fileContents.hasIntersectionWithComment(2, 1, 2, 10));
    assertTrue(fileContents.hasIntersectionWithComment(3, 1, 3, 10));
    assertTrue(fileContents.hasIntersectionWithComment(4, 1, 5, 30));
    assertFalse(fileContents.hasIntersectionWithComment(6, 1, 6, 10));
  }
  
  @Test
  public void testInPackageInfo() {
    assertFalse(fileContents.inPackageInfo());
  }
  
}
