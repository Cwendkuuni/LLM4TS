package com.example.junit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.SerializableStringSerializer;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() throws Exception {
        // ...
    }

    @Test
    public void testCustomRootSeparator() throws Exception {
        // ...
    }

    @Test
    public void testIndentArraysWith() throws Exception {
        // ...
    }

    @Test
    public void testIndentObjectsWith() throws Exception {
        // ...
    }

    @Test
    public void testWithSpacesInObjectEntries() throws Exception {
        // ...
    }

    @Test
    public void testWithSeparators() throws Exception {
        // ...
    }

    @Test
    public void testThrowsExceptionWhenNegativeNestingLevel() {
        // ...
    }
}