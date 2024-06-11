
package net.sf.xisemele.impl;

import org.w3c.dom.*;
import java.util.*;

class OperationsHelperImpl implements OperationsHelper
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public List<Node> children(final Node node) {
        final ArrayList<Node> children = new ArrayList<Node>();
        final NodeList nodeList = node.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); ++index) {
            final Node item = nodeList.item(index);
            if (item.getNodeType() == 1) {
                children.add(item);
            }
        }
        return children;
    }
    
    @Override
    public Node find(final Document document, final String path) {
        final String[] names = path.split("/");
        Node found = null;
        if (names.length > 0 && names[0].equals(document.getDocumentElement().getNodeName())) {
            found = document.getDocumentElement();
            NodeList nodeList = found.getChildNodes();
            for (int nameIndex = 1; nameIndex < names.length; ++nameIndex) {
                for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); ++nodeIndex) {
                    if (nodeList.item(nodeIndex).getNodeName().equals(names[nameIndex])) {
                        found = nodeList.item(nodeIndex);
                        nodeList = found.getChildNodes();
                        break;
                    }
                }
            }
        }
        if (found != null && found.getNodeName().equals(names[names.length - 1])) {
            return found;
        }
        return null;
    }
    
    @Override
    public Node nodeWithName(final List<Node> nodes, final String name) {
        for (final Node node : nodes) {
            if (node.getNodeName().equals(name)) {
                return node;
            }
        }
        return null;
    }
    
    @Override
    public List<Node> sublistWithName(final List<Node> nodes, final String name) {
        final ArrayList<Node> sublist = new ArrayList<Node>();
        for (final Node node : nodes) {
            if (node.getNodeName().equals(name)) {
                sublist.add(node);
            }
        }
        return sublist;
    }
}
