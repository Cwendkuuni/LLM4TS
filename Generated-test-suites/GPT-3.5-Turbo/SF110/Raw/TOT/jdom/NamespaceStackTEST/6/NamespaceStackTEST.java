import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NamespaceStackTest {
    
    private NamespaceStack namespaceStack;
    
    @Before
    public void setup() {
        namespaceStack = new NamespaceStack();
    }
    
    @Test
    public void testPush_AddElementWithMultipleNamespaces_AllNamespacesAddedToStack() {
        Element element = mock(Element.class);
        Namespace primaryNamespace = mock(Namespace.class);
        Namespace secondaryNamespace = mock(Namespace.class);
        when(element.getNamespace()).thenReturn(primaryNamespace);
        when(element.getAdditionalNamespaces()).thenReturn(new Namespace[] {secondaryNamespace});
        
        namespaceStack.push(element);
        
        verify(element, times(1)).getNamespace();
        verify(element, times(1)).getAdditionalNamespaces();
    }
    
    @Test
    public void testPush_AddElementWithNoNamespaces_NoNamespacesAddedToStack() {
        Element element = mock(Element.class);
        when(element.getNamespace()).thenReturn(Namespace.NO_NAMESPACE);
        
        namespaceStack.push(element);
        
        // Verifying that no additional namespace related methods are called
        verify(element, times(1)).getNamespace();
        verify(element, never()).getAdditionalNamespaces();
        verify(element, never()).hasAdditionalNamespaces();
        verify(element, never()).hasAttributes();
    }
    
    @Test
    public void testPush_AddElementWithAttributes_AttributesNamespacesAddedToStack() {
        Element element = mock(Element.class);
        Attribute attribute = mock(Attribute.class);
        Namespace attributeNamespace = mock(Namespace.class);
        when(element.getNamespace()).thenReturn(mock(Namespace.class));
        when(element.getAttributes()).thenReturn(new Attribute[] {attribute});
        when(attribute.getNamespace()).thenReturn(attributeNamespace);
        
        namespaceStack.push(element);
        
        verify(attribute, times(1)).getNamespace();
        verify(element, times(1)).getAttributes();
    } 
    
    @Test
    public void testPush_PushStackCalled_AddedAndScopeArraysUpdated() {
        Element element = mock(Element.class);
        Namespace primaryNamespace = mock(Namespace.class);
        when(element.getNamespace()).thenReturn(primaryNamespace);
        when(element.getAdditionalNamespaces()).thenReturn(new Namespace[] {mock(Namespace.class)});
        
        namespaceStack.push(element);
        
        Namespace[] addedArray = namespaceStack.getAddedArray();
        Namespace[] scopeArray = namespaceStack.getScopeArray();
        
        // Verifying that pushStack function is called and arrays are updated
        assertThat(addedArray.length, equalTo(1));
        assertThat(scopeArray.length, equalTo(1));
        verify(namespaceStack, times(1)).pushStack(eq(primaryNamespace), any(Namespace[].class), any(List.class));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop_StackEmpty_ThrowIllegalStateException() {
        namespaceStack.pop(); // Stack is initially empty, so pop should throw exception
    }
    
    @Test
    public void testPop_StackNotEmpty_PopSuccessful() {
        namespaceStack.push(mock(Element.class));
        
        namespaceStack.pop();
        
        assertThat(namespaceStack.getScope().length, equalTo(0));
        assertThat(namespaceStack.getAddedArray().length, equalTo(0));
    }
    
    @Test
    public void testAddedForward_AddedIsEmpty_ReturnEmptyIterator() {
        Iterable<Namespace> addedForward = namespaceStack.addedForward();
        
        assertThat(addedForward.iterator().hasNext(), equalTo(false));
    }
    
    @Test
    public void testAddedForward_AddedIsNotEmpty_ReturnForwardIterator() {
        Namespace namespace1 = mock(Namespace.class);
        Namespace namespace2 = mock(Namespace.class);
        Namespace namespace3 = mock(Namespace.class);
        namespaceStack.push(mock(Element.class));
        namespaceStack.push(mock(Element.class));
        namespaceStack.push(mock(Element.class));
        Namespace[] scope = new Namespace[] {namespace1, namespace2, namespace3};
        when(namespaceStack.getScope()).thenReturn(scope);
        
        Iterable<Namespace> addedForward = namespaceStack.addedForward();
        
        assertThat(addedForward.iterator().next(), equalTo(namespace1));
        assertThat(addedForward.iterator().next(), equalTo(namespace2));
        assertThat(addedForward.iterator().next(), equalTo(namespace3));
    }
    
    @Test
    public void testAddedReverse_AddedIsEmpty_ReturnEmptyIterator() {
        Iterable<Namespace> addedReverse = namespaceStack.addedReverse();
        
        assertThat(addedReverse.iterator().hasNext(), equalTo(false));
    }
    
    @Test
    public void testAddedReverse_AddedIsNotEmpty_ReturnReverseIterator() {
        Namespace namespace1 = mock(Namespace.class);
        Namespace namespace2 = mock(Namespace.class);
        Namespace namespace3 = mock(Namespace.class);
        namespaceStack.push(mock(Element.class));
        namespaceStack.push(mock(Element.class));
        namespaceStack.push(mock(Element.class));
        Namespace[] scope = new Namespace[] {namespace1, namespace2, namespace3};
        when(namespaceStack.getScope()).thenReturn(scope);
        
        Iterable<Namespace> addedReverse = namespaceStack.addedReverse();
        
        assertThat(addedReverse.iterator().next(), equalTo(namespace3));
        assertThat(addedReverse.iterator().next(), equalTo(namespace2));
        assertThat(addedReverse.iterator().next(), equalTo(namespace1));
    }
    
    @Test
    public void testIterator_CallIterator_MethodsCalledOnMockIterator() {
        Namespace namespace1 = mock(Namespace.class);
        Namespace namespace2 = mock(Namespace.class);
        namespaceStack.push(mock(Element.class));
        Namespace[] scope = new Namespace[] {namespace1, namespace2};
        when(namespaceStack.getScope()).thenReturn(scope);
        
        Iterator<Namespace> iterator = namespaceStack.iterator();
        iterator.hasNext();
        
        iterator.next();
        iterator.remove();
        
        verify(iterator, times(1)).hasNext();
        verify(iterator, times(1)).next();
        verify(iterator, times(1)).remove();
    }
    
    @Test
    public void testGetScope_ReturnCopyOfScope() {
        namespaceStack.push(mock(Element.class));
        Namespace[] scope = new Namespace[] {mock(Namespace.class)};
        when(namespaceStack.getScope()).thenReturn(scope);
        
        Namespace[] result = namespaceStack.getScope();
        
        assertThat(result, not(sameInstance(scope)));
    }
    
    @Test
    public void testIsInScope_NamespaceInScope_ReturnTrue() {
        Namespace namespace = mock(Namespace.class);
        namespaceStack.push(mock(Element.class));
        Namespace[] scope = new Namespace[] {namespace};
        when(namespaceStack.getScope()).thenReturn(scope);
        
        boolean result = namespaceStack.isInScope(namespace);
        
        assertThat(result, equalTo(true));
    }
    
    @Test
    public void testIsInScope_NamespaceNotInScope_ReturnFalse() {
        Namespace namespace = mock(Namespace.class);
        Namespace notInScopeNamespace = mock(Namespace.class);
        namespaceStack.push(mock(Element.class));
        Namespace[] scope = new Namespace[] {notInScopeNamespace};
        when(namespaceStack.getScope()).thenReturn(scope);
        
        boolean result = namespaceStack.isInScope(namespace);
        
        assertThat(result, equalTo(false));
    }
}