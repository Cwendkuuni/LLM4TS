package com.lts.event;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelper extends ListenerHelper implements KeyListener
{
    private Component component;
    
    public SimpleKeyListenerHelper(final Component component) {
        component.addKeyListener(this);
        this.component = component;
    }
    
    public void detach() {
        if (null != this.component) {
            this.component.removeKeyListener(this);
            this.component = null;
        }
    }
    
    @Override
    public void notifyListener(final Object o, final int type, final Object data) {
        final SimpleKeyListener listener = (SimpleKeyListener)o;
        switch (type) {
            case 10: {
                listener.enter(this.component);
                break;
            }
            case 155: {
                listener.insert(this.component);
                break;
            }
            case 127: {
                listener.delete(this.component);
                break;
            }
            case 9: {
                listener.tab(this.component);
                break;
            }
        }
    }
    
    @Override
    public void keyPressed(final KeyEvent event) {
        switch (event.getKeyCode()) {
            case 9:
            case 10:
            case 127:
            case 155: {
                this.fire(event.getKeyCode());
                break;
            }
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent arg0) {
    }
    
    @Override
    public void keyTyped(final KeyEvent event) {
    }
}
