import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component mockComponent;
    private SimpleKeyListener mockKeyListener;

    @Before
    public void setUp() {
        mockComponent = mock(Component.class);
        mockKeyListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
        simpleKeyListenerHelper.attach(mockKeyListener);
    }

    @Test
    public void detach_ComponentNotNull_Success() {
        simpleKeyListenerHelper.detach();

        verify(mockComponent, times(1)).removeKeyListener(simpleKeyListenerHelper);
        verifyNoMoreInteractions(mockComponent);
    }

    @Test
    public void detach_ComponentNull_NoException() {
        simpleKeyListenerHelper.detach();

        verify(mockComponent, never()).removeKeyListener(simpleKeyListenerHelper);
        verifyNoMoreInteractions(mockComponent);
    }

    @Test
    public void notifyListener_Type10_CallsEnterMethod() {
        Object mockObject = mock(Object.class);
        int type = 10;
        Object data = mock(Object.class);

        SimpleKeyListener simpleKeyListener = spy(SimpleKeyListener.class);
        simpleKeyListenerHelper.attach(simpleKeyListener);

        simpleKeyListenerHelper.notifyListener(mockObject, type, data);

        verify(simpleKeyListener, times(1)).enter(mockComponent);
        verifyNoMoreInteractions(simpleKeyListener);
    }

    @Test
    public void notifyListener_Type155_CallsInsertMethod() {
        Object mockObject = mock(Object.class);
        int type = 155;
        Object data = mock(Object.class);

        SimpleKeyListener simpleKeyListener = spy(SimpleKeyListener.class);
        simpleKeyListenerHelper.attach(simpleKeyListener);

        simpleKeyListenerHelper.notifyListener(mockObject, type, data);

        verify(simpleKeyListener, times(1)).insert(mockComponent);
        verifyNoMoreInteractions(simpleKeyListener);
    }

    @Test
    public void notifyListener_Type127_CallsDeleteMethod() {
        Object mockObject = mock(Object.class);
        int type = 127;
        Object data = mock(Object.class);

        SimpleKeyListener simpleKeyListener = spy(SimpleKeyListener.class);
        simpleKeyListenerHelper.attach(simpleKeyListener);

        simpleKeyListenerHelper.notifyListener(mockObject, type, data);

        verify(simpleKeyListener, times(1)).delete(mockComponent);
        verifyNoMoreInteractions(simpleKeyListener);
    }

    @Test
    public void notifyListener_Type9_CallsTabMethod() {
        Object mockObject = mock(Object.class);
        int type = 9;
        Object data = mock(Object.class);

        SimpleKeyListener simpleKeyListener = spy(SimpleKeyListener.class);
        simpleKeyListenerHelper.attach(simpleKeyListener);

        simpleKeyListenerHelper.notifyListener(mockObject, type, data);

        verify(simpleKeyListener, times(1)).tab(mockComponent);
        verifyNoMoreInteractions(simpleKeyListener);
    }

    @Test
    public void keyPressed_KeyCode9_CallsFireMethod() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(9);

        simpleKeyListenerHelper.keyPressed(mockKeyEvent);

        verify(simpleKeyListenerHelper, times(1)).fire(9);
    }

    @Test
    public void keyPressed_KeyCode10_CallsFireMethod() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(10);

        simpleKeyListenerHelper.keyPressed(mockKeyEvent);

        verify(simpleKeyListenerHelper, times(1)).fire(10);
    }

    @Test
    public void keyPressed_KeyCode127_CallsFireMethod() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(127);

        simpleKeyListenerHelper.keyPressed(mockKeyEvent);

        verify(simpleKeyListenerHelper, times(1)).fire(127);
    }

    @Test
    public void keyPressed_KeyCode155_CallsFireMethod() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(155);

        simpleKeyListenerHelper.keyPressed(mockKeyEvent);

        verify(simpleKeyListenerHelper, times(1)).fire(155);
    }

    // Additional Edge Case tests can be added if needed

}