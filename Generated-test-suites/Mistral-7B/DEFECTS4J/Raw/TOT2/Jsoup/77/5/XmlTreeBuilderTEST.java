package org.junit.test;

import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

private XmlTreeBuilder builder = new XmlTreeBuilder();

@Test
public void testDefaultSettings() {
// implementation here
}

@Test
public void testParseReader() {
// implementation here
}

@Test
public void testParseString() {
// implementation here
}

@Test
public void testInitialiseParse() {
// implementation here
}

@Test
public void testProcessStartTag() {
// implementation here
}

@Test
public void testInsertNode() {
// implementation here
}

@Test
public void testInsertStartTag() {
// implementation here
}

@Test
public void testInsertComment() {
// implementation here
}

@Test
public void testInsertCharacter() {
// implementation here
}

@Test
public void testInsertDoctype() {
// implementation here
}

@Test
public void testPopStackToClose() {
// implementation here
}

@Test
public void testParseFragment() {
// implementation here
}

@Test (expected = IllegalArgumentException.class)
public void testParseNullReader() {
// implementation here
}

@Test (expected = IllegalArgumentException.class)
public void testParseEmptyReader() {
// implementation here
}

@Test (expected = IllegalArgumentException.class)
public void testParseNullString() {
// implementation here
}

@Test (expected = IllegalArgumentException.class)
public void testParseEmptyString() {
// implementation here
}

@Test (expected = IllegalArgumentException.class)
public void testParseInvalidSettings() {
// implementation here
}

@Test (expected = IllegalArgumentException.class)
public void testProcessInvalidTokenType() {
// implementation here
}

@Test (expected = IllegalArgumentException.class)
public void testInsertUnknownSelfClosingTag() {
// implementation here
}

@Test (expected = IllegalArgumentException.class)
public void testInsertBogusXMLDeclaration() {
// implementation here
}

@Test (expected = IllegalArgumentException.class)
public void testInsertInvalidCharacterData() {
// implementation here
}

}