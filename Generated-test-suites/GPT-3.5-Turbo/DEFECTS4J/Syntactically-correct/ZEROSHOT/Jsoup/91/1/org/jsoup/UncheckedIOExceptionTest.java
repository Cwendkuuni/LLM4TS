package org.jsoup;

import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {

  @Test
  public void testIoException() {
    IOException ioException = new IOException("Test IO Exception");
    UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);

    IOException result = uncheckedIOException.ioException();

    assertEquals(ioException, result);
  }
}