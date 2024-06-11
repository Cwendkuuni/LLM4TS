package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

public class ExpressionTest {
    
    private Expression expression;
    
    @Before
    public void setUp() {
        expression = new TestExpression();
    }
    
    @Test
    public void testIsContextDependent() {
        assertTrue(expression.isContextDependent());
        assertFalse(new EmptyExpression().isContextDependent());
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.setRootContext(new RootContext());
        Object result = expression.computeValue(context);
        assertEquals("test", result);
    }
    
    @Test
    public void testCompute() {
        EvalContext context = new EvalContext();
        context.setContextNodePointer(new NodePointer());
        Object result = expression.compute(context);
        assertEquals("test", result);
    }
    
    @Test
    public void testIterate() {
        EvalContext context = new EvalContext();
        context.setRootContext(new RootContext());
        Iterator<?> iterator = expression.iterate(context);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIteratePointers() {
        EvalContext context = new EvalContext();
        context.setRootContext(new RootContext());
        Iterator<?> iterator = expression.iteratePointers(context);
        assertFalse(iterator.hasNext());
    }
    
    private static class TestExpression extends Expression {
        
        public boolean computeContextDependent() {
            return true;
        }
        
        public Object computeValue(EvalContext context) {
            return "test";
        }
        
        public Object compute(EvalContext context) {
            return "test";
        }
    }
    
    private static class EmptyExpression extends Expression {
        
        public boolean computeContextDependent() {
            return false;
        }
        
        public Object computeValue(EvalContext context) {
            return null;
        }
        
        public Object compute(EvalContext context) {
            return null;
        }
    }
    
    private static class RootContext extends org.apache.commons.jxpath.ri.JXPathContextReferenceImpl.RootContext {
        
        public Object getCurrentNode() {
            return null;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer getCurrentNodePointer() {
            return null;
        }
        
        public org.apache.commons.jxpath.ri.NamespaceResolver getNamespaceResolver() {
            return null;
        }
        
        public org.apache.commons.jxpath.ri.QName getNodeName() {
            return null;
        }
        
        public Object getSingleNodePointer() {
            return null;
        }
        
        public org.apache.commons.jxpath.ri.QName getName() {
            return null;
        }
        
        public int getPosition() {
            return 0;
        }
        
        public int getSize() {
            return 0;
        }
        
        public boolean getPredicateValue() {
            return false;
        }
        
        public Object getVariableValue(String name) {
            return null;
        }
        
        public boolean isActual() {
            return false;
        }
        
        public boolean isCollection() {
            return false;
        }
        
        public boolean isLeaf() {
            return false;
        }
        
        public boolean isNode() {
            return false;
        }
        
        public boolean isOver() {
            return false;
        }
        
        public boolean isRelative() {
            return false;
        }
        
        public boolean isRecovered() {
            return false;
        }
        
        public boolean isTransparent() {
            return false;
        }
        
        public void restoreState(Object state) {
            
        }
        
        public Object saveState() {
            return null;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer setValue(Object value) {
            return null;
        }
        
        public void subtreeMayHaveChanged() {
            
        }
        
        public void setVariableValue(String name, Object value) {
            
        }
        
        public Iterator<org.apache.commons.jxpath.Pointer> attributePointers() {
            return Collections.emptyIterator();
        }
        
        public Iterator<org.apache.commons.jxpath.Pointer> namespacePointers() {
            return Collections.emptyIterator();
        }
        
        public boolean compareChildNodePointers(
                org.apache.commons.jxpath.ri.model.NodePointer pointer1,
                org.apache.commons.jxpath.ri.model.NodePointer pointer2) {
            return false;
        }
        
        public boolean containsNode() {
            return false;
        }
        
        public boolean createChildNode(Object name, int index, Object value) {
            return false;
        }
        
        public boolean createChildNode(Object name, Object value) {
            return false;
        }
        
        public boolean equals(org.apache.commons.jxpath.Pointer pointer) {
            return false;
        }
        
        public Object getNode() {
            return null;
        }
        
        public org.apache.commons.jxpath.util.NamespaceResolver getNamespaceResolver() {
            return null;
        }
        
        public Object getSingleNode() {
            return null;
        }
        
        public Object getValue() {
            return null;
        }
        
        public org.apache.commons.jxpath.ri.QName getName(int index) {
            return null;
        }
        
        public String getNamespaceURI(String prefix) {
            return null;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer getParent() {
            return null;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer getPointerByID(String id) {
            return null;
        }
        
        public String getTextValue() {
            return null;
        }
        
        public Object getThis() {
            return null;
        }
        
        public boolean hasName(String namespaceURI, String name) {
            return false;
        }
        
        public boolean hasNamespace(String prefix) {
            return false;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer intern() {
            return null;
        }
        
        public boolean isAttribute() {
            return false;
        }
        
        public boolean isContainer() {
            return false;
        }
        
        public boolean isDocument() {
            return false;
        }
        
        public boolean isIgnoredNode() {
            return false;
        }
        
        public boolean isNamespace() {
            return false;
        }
        
        public boolean isText() {
            return false;
        }
        
        public Iterator<org.apache.commons.jxpath.Pointer> namespacePointers(String prefix) {
            return null;
        }
        
        public int namespacePointerCount() {
            return 0;
        }
        
        public Iterator<org.apache.commons.jxpath.Pointer> attributePointers(
                String namespaceURI,
                String name) {
            return null;
        }
        
        public int getAttributeCount() {
            return 0;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer getAttribute(int index) {
            return null;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer getAttribute(String namespaceURI, String name) {
            return null;
        }
        
        public int getNamespaceCount() {
            return 0;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer getNamespace(
                int index) {
                    return null;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer getNamespace(
                String prefix) {
                    return null;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer getRootElementPointer() {
            return null;
        }
        
        public org.apache.commons.jxpath.ri.model.NodePointer getValuePointer() {
            return null;
        }
    }
}