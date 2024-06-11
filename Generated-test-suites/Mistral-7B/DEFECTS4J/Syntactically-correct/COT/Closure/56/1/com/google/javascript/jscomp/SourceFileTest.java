package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {
  private SourceFile sourceFile;
  private Generator generatorMock;

  @Before
  public void setup() {
    generatorMock = mock(Generator.class);
    when(generatorMock.getCode()).thenReturn("mockCode");

    sourceFile = new SourceFile("testFileName") {
      @Override
      public SourceFile fromGenerator(String fileName, Generator generator) {
        return new Generated(fileName, generator);
      }
    };

    sourceFile.setGenerator(generatorMock);
  }

  @Test
  public void testGetFileName() {
    assertEquals("testFileName", sourceFile.getName());
  }

  @Test
  public void testGetCode() throws IOException {
    assertEquals("mockCode", sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    Reader reader = sourceFile.getCodeReader();
    assertEquals("mockCode", CharStreams.toString(reader));
  }

  @Test
  public void testGetOriginalPath() {
    assertEquals("testFileName", sourceFile.getOriginalPath());
  }

  @Test
  public void testSetOriginalPath() {
    sourceFile.setOriginalPath("testOriginalPath");
    assertEquals("testOriginalPath", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() {
    String originalCode = sourceFile.getCode();
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
    assertEquals(originalCode, sourceFile.getCodeNoCache());
  }

  @Test
  public void testHasSourceInMemory() {
    sourceFile.setCode("testCode");
    assertTrue(sourceFile.hasSourceInMemory());
    assertFalse(sourceFile.hasSourceInMemory());
  }
}