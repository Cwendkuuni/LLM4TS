package twitter4j;

import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import twitter4j.auth.*;

abstract class TwitterBaseImpl implements TwitterBase, Serializable, OAuthSupport, OAuth2Support, HttpResponseListener
{
    private static final String WWW_DETAILS = "See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/";
    private static final long serialVersionUID = -7824361938865528554L;
    Configuration conf;
    private transient String screenName;
    private transient long id;
    transient HttpClient http;
    private List<RateLimitStatusListener> rateLimitStatusListeners;
    ObjectFactory factory;
    Authorization auth;
    
    TwitterBaseImpl(final Configuration conf, final Authorization auth) {
        this.screenName = null;
        this.id = 0L;
        this.rateLimitStatusListeners = new ArrayList<RateLimitStatusListener>(0);
        this.conf = conf;
        this.auth = auth;
        this.init();
    }
    
    private void init() {
        if (null == this.auth) {
            final String consumerKey = this.conf.getOAuthConsumerKey();
            final String consumerSecret = this.conf.getOAuthConsumerSecret();
            if (consumerKey != null && consumerSecret != null) {
                if (this.conf.isApplicationOnlyAuthEnabled()) {
                    final OAuth2Authorization oauth2 = new OAuth2Authorization(this.conf);
                    final String tokenType = this.conf.getOAuth2TokenType();
                    final String accessToken = this.conf.getOAuth2AccessToken();
                    if (tokenType != null && accessToken != null) {
                        oauth2.setOAuth2Token(new OAuth2Token(tokenType, accessToken));
                    }
                    this.auth = oauth2;
                }
                else {
                    final OAuthAuthorization oauth3 = new OAuthAuthorization(this.conf);
                    final String accessToken2 = this.conf.getOAuthAccessToken();
                    final String accessTokenSecret = this.conf.getOAuthAccessTokenSecret();
                    if (accessToken2 != null && accessTokenSecret != null) {
                        oauth3.setOAuthAccessToken(new AccessToken(accessToken2, accessTokenSecret));
                    }
                    this.auth = oauth3;
                }
            }
            else {
                this.auth = NullAuthorization.getInstance();
            }
        }
        this.http = HttpClientFactory.getInstance(this.conf.getHttpClientConfiguration());
        this.setFactory();
    }
    
    void setFactory() {
        this.factory = new JSONImplFactory(this.conf);
    }
    
    public String getScreenName() throws TwitterException, IllegalStateException {
        if (!this.auth.isEnabled()) {
            throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (null == this.screenName) {
            if (this.auth instanceof BasicAuthorization) {
                this.screenName = ((BasicAuthorization)this.auth).getUserId();
                if (this.screenName.contains("@")) {
                    this.screenName = null;
                }
            }
            if (null == this.screenName) {
                this.fillInIDAndScreenName();
            }
        }
        return this.screenName;
    }
    
    public long getId() throws TwitterException, IllegalStateException {
        if (!this.auth.isEnabled()) {
            throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (0L == this.id) {
            this.fillInIDAndScreenName();
        }
        return this.id;
    }
    
    User fillInIDAndScreenName() throws TwitterException {
        return this.fillInIDAndScreenName(null);
    }
    
    User fillInIDAndScreenName(final HttpParameter[] parameters) throws TwitterException {
        this.ensureAuthorizationEnabled();
        final User user = new UserJSONImpl(this.http.get(this.conf.getRestBaseURL() + "account/verify_credentials.json", parameters, this.auth, this), this.conf);
        this.screenName = user.getScreenName();
        this.id = user.getId();
        return user;
    }
    
    public void addRateLimitStatusListener(final RateLimitStatusListener listener) {
        this.rateLimitStatusListeners.add(listener);
    }
    
    public void onRateLimitStatus(final Consumer<RateLimitStatusEvent> action) {
        this.rateLimitStatusListeners.add(new RateLimitStatusListener() {
            public void onRateLimitStatus(final RateLimitStatusEvent event) {
                action.accept(event);
            }
            
            public void onRateLimitReached(final RateLimitStatusEvent event) {
            }
        });
    }
    
    public void onRateLimitReached(final Consumer<RateLimitStatusEvent> action) {
        this.rateLimitStatusListeners.add(new RateLimitStatusListener() {
            public void onRateLimitStatus(final RateLimitStatusEvent event) {
            }
            
            public void onRateLimitReached(final RateLimitStatusEvent event) {
                action.accept(event);
            }
        });
    }
    
    public void httpResponseReceived(final HttpResponseEvent event) {
        if (this.rateLimitStatusListeners.size() != 0) {
            final HttpResponse res = event.getResponse();
            final TwitterException te = event.getTwitterException();
            RateLimitStatus rateLimitStatus;
            int statusCode;
            if (te != null) {
                rateLimitStatus = te.getRateLimitStatus();
                statusCode = te.getStatusCode();
            }
            else {
                rateLimitStatus = JSONImplFactory.createRateLimitStatusFromResponseHeader(res);
                statusCode = res.getStatusCode();
            }
            if (rateLimitStatus != null) {
                final RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(this, rateLimitStatus, event.isAuthenticated());
                if (statusCode == 420 || statusCode == 503 || statusCode == 429) {
                    for (final RateLimitStatusListener listener : this.rateLimitStatusListeners) {
                        listener.onRateLimitStatus(statusEvent);
                        listener.onRateLimitReached(statusEvent);
                    }
                }
                else {
                    for (final RateLimitStatusListener listener : this.rateLimitStatusListeners) {
                        listener.onRateLimitStatus(statusEvent);
                    }
                }
            }
        }
    }
    
    public final Authorization getAuthorization() {
        return this.auth;
    }
    
    public Configuration getConfiguration() {
        return this.conf;
    }
    
    final void ensureAuthorizationEnabled() {
        if (!this.auth.isEnabled()) {
            throw new IllegalStateException("Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/");
        }
    }
    
    final void ensureOAuthEnabled() {
        if (!(this.auth instanceof OAuthAuthorization)) {
            throw new IllegalStateException("OAuth required. Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/");
        }
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.putFields();
        out.writeFields();
        out.writeObject(this.conf);
        out.writeObject(this.auth);
        final List<RateLimitStatusListener> serializableRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>(0);
        for (final RateLimitStatusListener listener : this.rateLimitStatusListeners) {
            if (listener instanceof Serializable) {
                serializableRateLimitStatusListeners.add(listener);
            }
        }
        out.writeObject(serializableRateLimitStatusListeners);
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.readFields();
        this.conf = (Configuration)stream.readObject();
        this.auth = (Authorization)stream.readObject();
        this.rateLimitStatusListeners = (List<RateLimitStatusListener>)stream.readObject();
        this.http = HttpClientFactory.getInstance(this.conf.getHttpClientConfiguration());
        this.setFactory();
    }
    
    public synchronized void setOAuthConsumer(final String consumerKey, final String consumerSecret) {
        if (null == consumerKey) {
            throw new NullPointerException("consumer key is null");
        }
        if (null == consumerSecret) {
            throw new NullPointerException("consumer secret is null");
        }
        if (this.auth instanceof NullAuthorization) {
            if (this.conf.isApplicationOnlyAuthEnabled()) {
                final OAuth2Authorization oauth2 = new OAuth2Authorization(this.conf);
                oauth2.setOAuthConsumer(consumerKey, consumerSecret);
                this.auth = oauth2;
            }
            else {
                final OAuthAuthorization oauth3 = new OAuthAuthorization(this.conf);
                oauth3.setOAuthConsumer(consumerKey, consumerSecret);
                this.auth = oauth3;
            }
        }
        else if (this.auth instanceof BasicAuthorization) {
            final XAuthAuthorization xauth = new XAuthAuthorization((BasicAuthorization)this.auth);
            xauth.setOAuthConsumer(consumerKey, consumerSecret);
            this.auth = xauth;
        }
        else if (this.auth instanceof OAuthAuthorization || this.auth instanceof OAuth2Authorization) {
            throw new IllegalStateException("consumer key/secret pair already set.");
        }
    }
    
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return this.getOAuthRequestToken(null);
    }
    
    public RequestToken getOAuthRequestToken(final String callbackUrl) throws TwitterException {
        return this.getOAuth().getOAuthRequestToken(callbackUrl);
    }
    
    public RequestToken getOAuthRequestToken(final String callbackUrl, final String xAuthAccessType) throws TwitterException {
        return this.getOAuth().getOAuthRequestToken(callbackUrl, xAuthAccessType);
    }
    
    public RequestToken getOAuthRequestToken(final String callbackUrl, final String xAuthAccessType, final String xAuthMode) throws TwitterException {
        return this.getOAuth().getOAuthRequestToken(callbackUrl, xAuthAccessType, xAuthMode);
    }
    
    public synchronized AccessToken getOAuthAccessToken() throws TwitterException {
        Authorization auth = this.getAuthorization();
        AccessToken oauthAccessToken;
        if (auth instanceof BasicAuthorization) {
            final BasicAuthorization basicAuth = (BasicAuthorization)auth;
            auth = AuthorizationFactory.getInstance(this.conf);
            if (!(auth instanceof OAuthAuthorization)) {
                throw new IllegalStateException("consumer key / secret combination not supplied.");
            }
            this.auth = auth;
            final OAuthAuthorization oauthAuth = (OAuthAuthorization)auth;
            oauthAccessToken = oauthAuth.getOAuthAccessToken(basicAuth.getUserId(), basicAuth.getPassword());
        }
        else if (auth instanceof XAuthAuthorization) {
            final XAuthAuthorization xauth = (XAuthAuthorization)auth;
            this.auth = xauth;
            final OAuthAuthorization oauthAuth = new OAuthAuthorization(this.conf);
            oauthAuth.setOAuthConsumer(xauth.getConsumerKey(), xauth.getConsumerSecret());
            oauthAccessToken = oauthAuth.getOAuthAccessToken(xauth.getUserId(), xauth.getPassword());
        }
        else {
            oauthAccessToken = this.getOAuth().getOAuthAccessToken();
        }
        this.screenName = oauthAccessToken.getScreenName();
        this.id = oauthAccessToken.getUserId();
        return oauthAccessToken;
    }
    
    public synchronized AccessToken getOAuthAccessToken(final String oauthVerifier) throws TwitterException {
        final AccessToken oauthAccessToken = this.getOAuth().getOAuthAccessToken(oauthVerifier);
        this.screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }
    
    public synchronized AccessToken getOAuthAccessToken(final RequestToken requestToken) throws TwitterException {
        final OAuthSupport oauth = this.getOAuth();
        final AccessToken oauthAccessToken = oauth.getOAuthAccessToken(requestToken);
        this.screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }
    
    public synchronized AccessToken getOAuthAccessToken(final RequestToken requestToken, final String oauthVerifier) throws TwitterException {
        return this.getOAuth().getOAuthAccessToken(requestToken, oauthVerifier);
    }
    
    public synchronized void setOAuthAccessToken(final AccessToken accessToken) {
        this.getOAuth().setOAuthAccessToken(accessToken);
    }
    
    public synchronized AccessToken getOAuthAccessToken(final String screenName, final String password) throws TwitterException {
        return this.getOAuth().getOAuthAccessToken(screenName, password);
    }
    
    private OAuthSupport getOAuth() {
        if (!(this.auth instanceof OAuthSupport)) {
            throw new IllegalStateException("OAuth consumer key/secret combination not supplied");
        }
        return (OAuthSupport)this.auth;
    }
    
    public synchronized OAuth2Token getOAuth2Token() throws TwitterException {
        return this.getOAuth2().getOAuth2Token();
    }
    
    public void setOAuth2Token(final OAuth2Token oauth2Token) {
        this.getOAuth2().setOAuth2Token(oauth2Token);
    }
    
    public synchronized void invalidateOAuth2Token() throws TwitterException {
        this.getOAuth2().invalidateOAuth2Token();
    }
    
    private OAuth2Support getOAuth2() {
        if (!(this.auth instanceof OAuth2Support)) {
            throw new IllegalStateException("OAuth consumer key/secret combination not supplied");
        }
        return (OAuth2Support)this.auth;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TwitterBaseImpl)) {
            return false;
        }
        final TwitterBaseImpl that = (TwitterBaseImpl)o;
        Label_0054: {
            if (this.auth != null) {
                if (this.auth.equals(that.auth)) {
                    break Label_0054;
                }
            }
            else if (that.auth == null) {
                break Label_0054;
            }
            return false;
        }
        if (!this.conf.equals(that.conf)) {
            return false;
        }
        if (this.http != null) {
            if (this.http.equals(that.http)) {
                return this.rateLimitStatusListeners.equals(that.rateLimitStatusListeners);
            }
        }
        else if (that.http == null) {
            return this.rateLimitStatusListeners.equals(that.rateLimitStatusListeners);
        }
        return false;
    }
    
    public int hashCode() {
        int result = this.conf.hashCode();
        result = 31 * result + ((this.http != null) ? this.http.hashCode() : 0);
        result = 31 * result + this.rateLimitStatusListeners.hashCode();
        result = 31 * result + ((this.auth != null) ? this.auth.hashCode() : 0);
        return result;
    }
    
    public String toString() {
        return "TwitterBase{conf=" + this.conf + ", http=" + this.http + ", rateLimitStatusListeners=" + this.rateLimitStatusListeners + ", auth=" + this.auth + '}';
    }
}
