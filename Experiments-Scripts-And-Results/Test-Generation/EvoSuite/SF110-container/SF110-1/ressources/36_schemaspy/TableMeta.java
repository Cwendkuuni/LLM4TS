package net.sourceforge.schemaspy.model.xml;

import java.util.logging.*;
import java.util.*;
import org.w3c.dom.*;

public class TableMeta
{
    private final String name;
    private final String comments;
    private final List<TableColumnMeta> columns;
    private final String remoteCatalog;
    private final String remoteSchema;
    private static final Logger logger;
    
    TableMeta(final Node tableNode) {
        this.columns = new ArrayList<TableColumnMeta>();
        final NamedNodeMap attribs = tableNode.getAttributes();
        this.name = attribs.getNamedItem("name").getNodeValue();
        Node node = attribs.getNamedItem("comments");
        if (node == null) {
            node = attribs.getNamedItem("remarks");
        }
        if (node != null) {
            final String tmp = node.getNodeValue().trim();
            this.comments = ((tmp.length() == 0) ? null : tmp);
        }
        else {
            this.comments = null;
        }
        node = attribs.getNamedItem("remoteSchema");
        this.remoteSchema = ((node == null) ? null : node.getNodeValue().trim());
        node = attribs.getNamedItem("remoteCatalog");
        this.remoteCatalog = ((node == null) ? null : node.getNodeValue().trim());
        TableMeta.logger.fine("Found XML table metadata for " + this.name + " remoteCatalog: " + this.remoteCatalog + " remoteSchema: " + this.remoteSchema + " comments: " + this.comments);
        final NodeList columnNodes = ((Element)tableNode.getChildNodes()).getElementsByTagName("column");
        for (int i = 0; i < columnNodes.getLength(); ++i) {
            final Node colNode = columnNodes.item(i);
            this.columns.add(new TableColumnMeta(colNode));
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getComments() {
        return this.comments;
    }
    
    public List<TableColumnMeta> getColumns() {
        return this.columns;
    }
    
    public String getRemoteCatalog() {
        return this.remoteCatalog;
    }
    
    public String getRemoteSchema() {
        return this.remoteSchema;
    }
    
    static {
        logger = Logger.getLogger(TableMeta.class.getName());
    }
}
