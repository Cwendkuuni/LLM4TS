package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstruction() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("rootSeparator"));
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterSerialization() throws IOException, ClassNotFoundException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(prettyPrinter);
        objectOutputStream.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        DefaultPrettyPrinter deserializedPrettyPrinter = (DefaultPrettyPrinter) objectInputStream.readObject();
        objectInputStream.close();
        assertNotNull(deserializedPrettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new FixedSpaceIndenter();
        prettyPrinter.indentArraysWith(indenter);
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators();
        prettyPrinter.withSeparators(separators);
        assertNotNull(prettyPrinter);
    }
}