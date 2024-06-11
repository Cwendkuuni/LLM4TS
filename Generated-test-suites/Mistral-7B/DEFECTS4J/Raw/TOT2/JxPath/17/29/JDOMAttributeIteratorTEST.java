package org.apache.commons.jxpath.ri.model.jdom.test;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.JDOMDocument;
import org.apache.commons.jxpath.ri.model.NodeIteratorTestSupport;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.Document;
import org.junit.jupiter.api.*;

public class JDOMAttributeIteratorTest extends NodeIteratorTestSupport {

    @BeforeEach
    public void setUp() throws JDOMException, InstantiationException, IllegalAccessException {
        setUp("JDOMAttributeIterator.java", "JDOMAttributeIteratorTest.xml");
    }

    // Test methods from Expert 1 and any additional test cases here

    @AfterEach
    public void tearDown() throws Exception {
        tearDown();
    }
}