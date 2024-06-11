/*
 * $Id: StatefulXMLEventListener.java,v 1.2 2005/09/12 08:40:02 znerd Exp $
 */
package org.znerd.xmlenc;

/**
 * Stateful <code>XMLEventListener</code>. This interface adds a single
 * {@link #getState()}.
 *
 * @version $Revision: 1.2 $ $Date: 2005/09/12 08:40:02 $
 * @author Ernst de Haan (<a href="mailto:wfe.dehaan@gmail.com">wfe.dehaan@gmail.com</a>)
 *
 * @since xmlenc 0.32
 */
public interface StatefulXMLEventListener
extends XMLEventListener {

   /**
    * Returns the current state of this outputter.
    *
    * @return
    *    the current state, cannot be <code>null</code>.
    */
   XMLEventListenerState getState();
}
