package twitter4j;

import java.util.concurrent.*;
import twitter4j.conf.*;
import twitter4j.auth.*;
import java.net.*;
import java.io.*;
import java.util.*;
import twitter4j.api.*;

class TwitterImpl extends TwitterBaseImpl implements Twitter
{
    private static final long serialVersionUID = 9170943084096085770L;
    private final String IMPLICIT_PARAMS_STR;
    private final HttpParameter[] IMPLICIT_PARAMS;
    private final HttpParameter INCLUDE_MY_RETWEET;
    private static final ConcurrentHashMap<Configuration, HttpParameter[]> implicitParamsMap;
    private static final ConcurrentHashMap<Configuration, String> implicitParamsStrMap;
    
    TwitterImpl(final Configuration conf, final Authorization auth) {
        super(conf, auth);
        this.INCLUDE_MY_RETWEET = new HttpParameter("include_my_retweet", conf.isIncludeMyRetweetEnabled());
        if (TwitterImpl.implicitParamsMap.containsKey(conf)) {
            this.IMPLICIT_PARAMS = TwitterImpl.implicitParamsMap.get(conf);
            this.IMPLICIT_PARAMS_STR = TwitterImpl.implicitParamsStrMap.get(conf);
        }
        else {
            String implicitParamsStr = conf.isIncludeEntitiesEnabled() ? "include_entities=true" : "";
            final boolean contributorsEnabled = conf.getContributingTo() != -1L;
            if (contributorsEnabled) {
                if (!"".equals(implicitParamsStr)) {
                    implicitParamsStr += "?";
                }
                implicitParamsStr = implicitParamsStr + "contributingto=" + conf.getContributingTo();
            }
            final List<HttpParameter> params = new ArrayList<HttpParameter>(3);
            if (conf.isIncludeEntitiesEnabled()) {
                params.add(new HttpParameter("include_entities", "true"));
            }
            if (contributorsEnabled) {
                params.add(new HttpParameter("contributingto", conf.getContributingTo()));
            }
            if (conf.isTrimUserEnabled()) {
                params.add(new HttpParameter("trim_user", "1"));
            }
            if (conf.isIncludeExtAltTextEnabled()) {
                params.add(new HttpParameter("include_ext_alt_text", "true"));
            }
            final HttpParameter[] implicitParams = params.toArray(new HttpParameter[params.size()]);
            TwitterImpl.implicitParamsStrMap.putIfAbsent(conf, implicitParamsStr);
            TwitterImpl.implicitParamsMap.putIfAbsent(conf, implicitParams);
            this.IMPLICIT_PARAMS = implicitParams;
            this.IMPLICIT_PARAMS_STR = implicitParamsStr;
        }
    }
    
    public ResponseList<Status> getMentionsTimeline() throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/mentions_timeline.json"));
    }
    
    public ResponseList<Status> getMentionsTimeline(final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/mentions_timeline.json", paging.asPostParameterArray()));
    }
    
    public ResponseList<Status> getHomeTimeline() throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/home_timeline.json", this.INCLUDE_MY_RETWEET));
    }
    
    public ResponseList<Status> getHomeTimeline(final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/home_timeline.json", this.mergeParameters(paging.asPostParameterArray(), new HttpParameter[] { this.INCLUDE_MY_RETWEET })));
    }
    
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/retweets_of_me.json"));
    }
    
    public ResponseList<Status> getRetweetsOfMe(final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/retweets_of_me.json", paging.asPostParameterArray()));
    }
    
    public ResponseList<Status> getUserTimeline(final String screenName, final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/user_timeline.json", this.mergeParameters(new HttpParameter[] { new HttpParameter("screen_name", screenName), this.INCLUDE_MY_RETWEET }, paging.asPostParameterArray())));
    }
    
    public ResponseList<Status> getUserTimeline(final long userId, final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/user_timeline.json", this.mergeParameters(new HttpParameter[] { new HttpParameter("user_id", userId), this.INCLUDE_MY_RETWEET }, paging.asPostParameterArray())));
    }
    
    public ResponseList<Status> getUserTimeline(final String screenName) throws TwitterException {
        return this.getUserTimeline(screenName, new Paging());
    }
    
    public ResponseList<Status> getUserTimeline(final long userId) throws TwitterException {
        return this.getUserTimeline(userId, new Paging());
    }
    
    public ResponseList<Status> getUserTimeline() throws TwitterException {
        return this.getUserTimeline(new Paging());
    }
    
    public ResponseList<Status> getUserTimeline(final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/user_timeline.json", this.mergeParameters(new HttpParameter[] { this.INCLUDE_MY_RETWEET }, paging.asPostParameterArray())));
    }
    
    public ResponseList<Status> getRetweets(final long statusId) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/retweets/" + statusId + ".json?count=100"));
    }
    
    public IDs getRetweeterIds(final long statusId, final long cursor) throws TwitterException {
        return this.getRetweeterIds(statusId, 100, cursor);
    }
    
    public IDs getRetweeterIds(final long statusId, final int count, final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "statuses/retweeters/ids.json?id=" + statusId + "&cursor=" + cursor + "&count=" + count));
    }
    
    public Status showStatus(final long id) throws TwitterException {
        return this.factory.createStatus(this.get(this.conf.getRestBaseURL() + "statuses/show/" + id + ".json", this.INCLUDE_MY_RETWEET));
    }
    
    public Status destroyStatus(final long statusId) throws TwitterException {
        return this.factory.createStatus(this.post(this.conf.getRestBaseURL() + "statuses/destroy/" + statusId + ".json"));
    }
    
    public Status updateStatus(final String status) throws TwitterException {
        return this.factory.createStatus(this.post(this.conf.getRestBaseURL() + "statuses/update.json", new HttpParameter("status", status)));
    }
    
    public Status updateStatus(final StatusUpdate status) throws TwitterException {
        final String url = this.conf.getRestBaseURL() + (status.isForUpdateWithMedia() ? "statuses/update_with_media.json" : "statuses/update.json");
        return this.factory.createStatus(this.post(url, status.asHttpParameterArray()));
    }
    
    public Status retweetStatus(final long statusId) throws TwitterException {
        return this.factory.createStatus(this.post(this.conf.getRestBaseURL() + "statuses/retweet/" + statusId + ".json"));
    }
    
    public OEmbed getOEmbed(final OEmbedRequest req) throws TwitterException {
        return this.factory.createOEmbed(this.get(this.conf.getRestBaseURL() + "statuses/oembed.json", req.asHttpParameterArray()));
    }
    
    public ResponseList<Status> lookup(final long... ids) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "statuses/lookup.json?id=" + StringUtil.join(ids)));
    }
    
    public UploadedMedia uploadMedia(final File image) throws TwitterException {
        this.checkFileValidity(image);
        return new UploadedMedia(this.post(this.conf.getUploadBaseURL() + "media/upload.json", new HttpParameter("media", image)).asJSONObject());
    }
    
    public UploadedMedia uploadMedia(final String fileName, final InputStream image) throws TwitterException {
        return new UploadedMedia(this.post(this.conf.getUploadBaseURL() + "media/upload.json", new HttpParameter("media", fileName, image)).asJSONObject());
    }
    
    public QueryResult search(final Query query) throws TwitterException {
        if (query.nextPage() != null) {
            return this.factory.createQueryResult(this.get(this.conf.getRestBaseURL() + "search/tweets.json" + query.nextPage()), query);
        }
        return this.factory.createQueryResult(this.get(this.conf.getRestBaseURL() + "search/tweets.json", query.asHttpParameterArray()), query);
    }
    
    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        return this.factory.createDirectMessageList(this.get(this.conf.getRestBaseURL() + "direct_messages.json?full_text=true"));
    }
    
    public ResponseList<DirectMessage> getDirectMessages(final Paging paging) throws TwitterException {
        return this.factory.createDirectMessageList(this.get(this.conf.getRestBaseURL() + "direct_messages.json", this.mergeParameters(paging.asPostParameterArray(), new HttpParameter("full_text", true))));
    }
    
    public ResponseList<DirectMessage> getSentDirectMessages() throws TwitterException {
        return this.factory.createDirectMessageList(this.get(this.conf.getRestBaseURL() + "direct_messages/sent.json?full_text=true"));
    }
    
    public ResponseList<DirectMessage> getSentDirectMessages(final Paging paging) throws TwitterException {
        return this.factory.createDirectMessageList(this.get(this.conf.getRestBaseURL() + "direct_messages/sent.json", this.mergeParameters(paging.asPostParameterArray(), new HttpParameter("full_text", true))));
    }
    
    public DirectMessage showDirectMessage(final long id) throws TwitterException {
        return this.factory.createDirectMessage(this.get(this.conf.getRestBaseURL() + "direct_messages/show.json?id=" + id + "&full_text=true"));
    }
    
    public DirectMessage destroyDirectMessage(final long id) throws TwitterException {
        return this.factory.createDirectMessage(this.post(this.conf.getRestBaseURL() + "direct_messages/destroy.json?id=" + id + "&full_text=true"));
    }
    
    public DirectMessage sendDirectMessage(final long userId, final String text) throws TwitterException {
        return this.factory.createDirectMessage(this.post(this.conf.getRestBaseURL() + "direct_messages/new.json", new HttpParameter("user_id", userId), new HttpParameter("text", text), new HttpParameter("full_text", true)));
    }
    
    public DirectMessage sendDirectMessage(final String screenName, final String text) throws TwitterException {
        return this.factory.createDirectMessage(this.post(this.conf.getRestBaseURL() + "direct_messages/new.json", new HttpParameter("screen_name", screenName), new HttpParameter("text", text), new HttpParameter("full_text", true)));
    }
    
    public InputStream getDMImageAsStream(final String url) throws TwitterException {
        return this.get(url).asStream();
    }
    
    public IDs getNoRetweetsFriendships() throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "friendships/no_retweets/ids.json"));
    }
    
    public IDs getFriendsIDs(final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "friends/ids.json?cursor=" + cursor));
    }
    
    public IDs getFriendsIDs(final long userId, final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "friends/ids.json?user_id=" + userId + "&cursor=" + cursor));
    }
    
    public IDs getFriendsIDs(final long userId, final long cursor, final int count) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "friends/ids.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count));
    }
    
    public IDs getFriendsIDs(final String screenName, final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "friends/ids.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor)));
    }
    
    public IDs getFriendsIDs(final String screenName, final long cursor, final int count) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "friends/ids.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count)));
    }
    
    public IDs getFollowersIDs(final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "followers/ids.json?cursor=" + cursor));
    }
    
    public IDs getFollowersIDs(final long userId, final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "followers/ids.json?user_id=" + userId + "&cursor=" + cursor));
    }
    
    public IDs getFollowersIDs(final long userId, final long cursor, final int count) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "followers/ids.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count));
    }
    
    public IDs getFollowersIDs(final String screenName, final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "followers/ids.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor)));
    }
    
    public IDs getFollowersIDs(final String screenName, final long cursor, final int count) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "followers/ids.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count)));
    }
    
    public ResponseList<Friendship> lookupFriendships(final long... ids) throws TwitterException {
        return this.factory.createFriendshipList(this.get(this.conf.getRestBaseURL() + "friendships/lookup.json?user_id=" + StringUtil.join(ids)));
    }
    
    public ResponseList<Friendship> lookupFriendships(final String... screenNames) throws TwitterException {
        return this.factory.createFriendshipList(this.get(this.conf.getRestBaseURL() + "friendships/lookup.json?screen_name=" + StringUtil.join(screenNames)));
    }
    
    public IDs getIncomingFriendships(final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "friendships/incoming.json?cursor=" + cursor));
    }
    
    public IDs getOutgoingFriendships(final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "friendships/outgoing.json?cursor=" + cursor));
    }
    
    public User createFriendship(final long userId) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId));
    }
    
    public User createFriendship(final String screenName) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "friendships/create.json", new HttpParameter("screen_name", screenName)));
    }
    
    public User createFriendship(final long userId, final boolean follow) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId + "&follow=" + follow));
    }
    
    public User createFriendship(final String screenName, final boolean follow) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "friendships/create.json", new HttpParameter("screen_name", screenName), new HttpParameter("follow", follow)));
    }
    
    public User destroyFriendship(final long userId) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "friendships/destroy.json?user_id=" + userId));
    }
    
    public User destroyFriendship(final String screenName) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "friendships/destroy.json", new HttpParameter("screen_name", screenName)));
    }
    
    public Relationship updateFriendship(final long userId, final boolean enableDeviceNotification, final boolean retweets) throws TwitterException {
        return this.factory.createRelationship(this.post(this.conf.getRestBaseURL() + "friendships/update.json", new HttpParameter("user_id", userId), new HttpParameter("device", enableDeviceNotification), new HttpParameter("retweets", retweets)));
    }
    
    public Relationship updateFriendship(final String screenName, final boolean enableDeviceNotification, final boolean retweets) throws TwitterException {
        return this.factory.createRelationship(this.post(this.conf.getRestBaseURL() + "friendships/update.json", new HttpParameter("screen_name", screenName), new HttpParameter("device", enableDeviceNotification), new HttpParameter("retweets", retweets)));
    }
    
    public Relationship showFriendship(final long sourceId, final long targetId) throws TwitterException {
        return this.factory.createRelationship(this.get(this.conf.getRestBaseURL() + "friendships/show.json", new HttpParameter("source_id", sourceId), new HttpParameter("target_id", targetId)));
    }
    
    public Relationship showFriendship(final String sourceScreenName, final String targetScreenName) throws TwitterException {
        return this.factory.createRelationship(this.get(this.conf.getRestBaseURL() + "friendships/show.json", HttpParameter.getParameterArray("source_screen_name", sourceScreenName, "target_screen_name", targetScreenName)));
    }
    
    public PagableResponseList<User> getFriendsList(final long userId, final long cursor) throws TwitterException {
        return this.getFriendsList(userId, cursor, 20);
    }
    
    public PagableResponseList<User> getFriendsList(final long userId, final long cursor, final int count) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "friends/list.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count));
    }
    
    public PagableResponseList<User> getFriendsList(final String screenName, final long cursor) throws TwitterException {
        return this.getFriendsList(screenName, cursor, 20);
    }
    
    public PagableResponseList<User> getFriendsList(final String screenName, final long cursor, final int count) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "friends/list.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count)));
    }
    
    public PagableResponseList<User> getFriendsList(final long userId, final long cursor, final int count, final boolean skipStatus, final boolean includeUserEntities) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "friends/list.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count + "&skip_status=" + skipStatus + "&include_user_entities=" + includeUserEntities));
    }
    
    public PagableResponseList<User> getFriendsList(final String screenName, final long cursor, final int count, final boolean skipStatus, final boolean includeUserEntities) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "friends/list.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count), new HttpParameter("skip_status", skipStatus), new HttpParameter("include_user_entities", includeUserEntities)));
    }
    
    public PagableResponseList<User> getFollowersList(final long userId, final long cursor) throws TwitterException {
        return this.getFollowersList(userId, cursor, 20);
    }
    
    public PagableResponseList<User> getFollowersList(final String screenName, final long cursor) throws TwitterException {
        return this.getFollowersList(screenName, cursor, 20);
    }
    
    public PagableResponseList<User> getFollowersList(final long userId, final long cursor, final int count) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "followers/list.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count));
    }
    
    public PagableResponseList<User> getFollowersList(final String screenName, final long cursor, final int count) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "followers/list.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count)));
    }
    
    public PagableResponseList<User> getFollowersList(final long userId, final long cursor, final int count, final boolean skipStatus, final boolean includeUserEntities) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "followers/list.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count + "&skip_status=" + skipStatus + "&include_user_entities=" + includeUserEntities));
    }
    
    public PagableResponseList<User> getFollowersList(final String screenName, final long cursor, final int count, final boolean skipStatus, final boolean includeUserEntities) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "followers/list.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count), new HttpParameter("skip_status", skipStatus), new HttpParameter("include_user_entities", includeUserEntities)));
    }
    
    public AccountSettings getAccountSettings() throws TwitterException {
        return this.factory.createAccountSettings(this.get(this.conf.getRestBaseURL() + "account/settings.json"));
    }
    
    public User verifyCredentials() throws TwitterException {
        return super.fillInIDAndScreenName(new HttpParameter[] { new HttpParameter("include_email", this.conf.isIncludeEmailEnabled()) });
    }
    
    public AccountSettings updateAccountSettings(final Integer trend_locationWoeid, final Boolean sleep_timeEnabled, final String start_sleepTime, final String end_sleepTime, final String time_zone, final String lang) throws TwitterException {
        final List<HttpParameter> profile = new ArrayList<HttpParameter>(6);
        if (trend_locationWoeid != null) {
            profile.add(new HttpParameter("trend_location_woeid", trend_locationWoeid));
        }
        if (sleep_timeEnabled != null) {
            profile.add(new HttpParameter("sleep_time_enabled", sleep_timeEnabled.toString()));
        }
        if (start_sleepTime != null) {
            profile.add(new HttpParameter("start_sleep_time", start_sleepTime));
        }
        if (end_sleepTime != null) {
            profile.add(new HttpParameter("end_sleep_time", end_sleepTime));
        }
        if (time_zone != null) {
            profile.add(new HttpParameter("time_zone", time_zone));
        }
        if (lang != null) {
            profile.add(new HttpParameter("lang", lang));
        }
        return this.factory.createAccountSettings(this.post(this.conf.getRestBaseURL() + "account/settings.json", (HttpParameter[])profile.toArray(new HttpParameter[profile.size()])));
    }
    
    public User updateProfile(final String name, final String url, final String location, final String description) throws TwitterException {
        final List<HttpParameter> profile = new ArrayList<HttpParameter>(4);
        this.addParameterToList(profile, "name", name);
        this.addParameterToList(profile, "url", url);
        this.addParameterToList(profile, "location", location);
        this.addParameterToList(profile, "description", description);
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "account/update_profile.json", (HttpParameter[])profile.toArray(new HttpParameter[profile.size()])));
    }
    
    public User updateProfileBackgroundImage(final File image, final boolean tile) throws TwitterException {
        this.checkFileValidity(image);
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "account/update_profile_background_image.json", new HttpParameter("image", image), new HttpParameter("tile", tile)));
    }
    
    public User updateProfileBackgroundImage(final InputStream image, final boolean tile) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "account/update_profile_background_image.json", new HttpParameter("image", "image", image), new HttpParameter("tile", tile)));
    }
    
    public User updateProfileColors(final String profileBackgroundColor, final String profileTextColor, final String profileLinkColor, final String profileSidebarFillColor, final String profileSidebarBorderColor) throws TwitterException {
        throw new UnsupportedOperationException("this API is no longer supported. https://twittercommunity.com/t/deprecation-of-account-update-profile-colors/28692");
    }
    
    private void addParameterToList(final List<HttpParameter> colors, final String paramName, final String color) {
        if (color != null) {
            colors.add(new HttpParameter(paramName, color));
        }
    }
    
    public User updateProfileImage(final File image) throws TwitterException {
        this.checkFileValidity(image);
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "account/update_profile_image.json", new HttpParameter("image", image)));
    }
    
    public User updateProfileImage(final InputStream image) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "account/update_profile_image.json", new HttpParameter("image", "image", image)));
    }
    
    private void checkFileValidity(final File image) throws TwitterException {
        if (!image.exists()) {
            throw new TwitterException(new FileNotFoundException(image + " is not found."));
        }
        if (!image.isFile()) {
            throw new TwitterException(new IOException(image + " is not a file."));
        }
    }
    
    public PagableResponseList<User> getBlocksList() throws TwitterException {
        return this.getBlocksList(-1L);
    }
    
    public PagableResponseList<User> getBlocksList(final long cursor) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "blocks/list.json?cursor=" + cursor));
    }
    
    public IDs getBlocksIDs() throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "blocks/ids.json"));
    }
    
    public IDs getBlocksIDs(final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "blocks/ids.json?cursor=" + cursor));
    }
    
    public User createBlock(final long userId) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "blocks/create.json?user_id=" + userId));
    }
    
    public User createBlock(final String screenName) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "blocks/create.json", new HttpParameter("screen_name", screenName)));
    }
    
    public User destroyBlock(final long userId) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "blocks/destroy.json?user_id=" + userId));
    }
    
    public User destroyBlock(final String screenName) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "blocks/destroy.json", new HttpParameter("screen_name", screenName)));
    }
    
    public PagableResponseList<User> getMutesList(final long cursor) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "mutes/users/list.json?cursor=" + cursor));
    }
    
    public IDs getMutesIDs(final long cursor) throws TwitterException {
        return this.factory.createIDs(this.get(this.conf.getRestBaseURL() + "mutes/users/ids.json?cursor=" + cursor));
    }
    
    public User createMute(final long userId) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "mutes/users/create.json?user_id=" + userId));
    }
    
    public User createMute(final String screenName) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "mutes/users/create.json", new HttpParameter("screen_name", screenName)));
    }
    
    public User destroyMute(final long userId) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "mutes/users/destroy.json?user_id=" + userId));
    }
    
    public User destroyMute(final String screenName) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "mutes/users/destroy.json", new HttpParameter("screen_name", screenName)));
    }
    
    public ResponseList<User> lookupUsers(final long... ids) throws TwitterException {
        return this.factory.createUserList(this.get(this.conf.getRestBaseURL() + "users/lookup.json", new HttpParameter("user_id", StringUtil.join(ids))));
    }
    
    public ResponseList<User> lookupUsers(final String... screenNames) throws TwitterException {
        return this.factory.createUserList(this.get(this.conf.getRestBaseURL() + "users/lookup.json", new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }
    
    public User showUser(final long userId) throws TwitterException {
        return this.factory.createUser(this.get(this.conf.getRestBaseURL() + "users/show.json?user_id=" + userId));
    }
    
    public User showUser(final String screenName) throws TwitterException {
        return this.factory.createUser(this.get(this.conf.getRestBaseURL() + "users/show.json", new HttpParameter("screen_name", screenName)));
    }
    
    public ResponseList<User> searchUsers(final String query, final int page) throws TwitterException {
        return this.factory.createUserList(this.get(this.conf.getRestBaseURL() + "users/search.json", new HttpParameter("q", query), new HttpParameter("per_page", 20), new HttpParameter("page", page)));
    }
    
    public ResponseList<User> getContributees(final long userId) throws TwitterException {
        return this.factory.createUserList(this.get(this.conf.getRestBaseURL() + "users/contributees.json?user_id=" + userId));
    }
    
    public ResponseList<User> getContributees(final String screenName) throws TwitterException {
        return this.factory.createUserList(this.get(this.conf.getRestBaseURL() + "users/contributees.json", new HttpParameter("screen_name", screenName)));
    }
    
    public ResponseList<User> getContributors(final long userId) throws TwitterException {
        return this.factory.createUserList(this.get(this.conf.getRestBaseURL() + "users/contributors.json?user_id=" + userId));
    }
    
    public ResponseList<User> getContributors(final String screenName) throws TwitterException {
        return this.factory.createUserList(this.get(this.conf.getRestBaseURL() + "users/contributors.json", new HttpParameter("screen_name", screenName)));
    }
    
    public void removeProfileBanner() throws TwitterException {
        this.post(this.conf.getRestBaseURL() + "account/remove_profile_banner.json");
    }
    
    public void updateProfileBanner(final File image) throws TwitterException {
        this.checkFileValidity(image);
        this.post(this.conf.getRestBaseURL() + "account/update_profile_banner.json", new HttpParameter("banner", image));
    }
    
    public void updateProfileBanner(final InputStream image) throws TwitterException {
        this.post(this.conf.getRestBaseURL() + "account/update_profile_banner.json", new HttpParameter("banner", "banner", image));
    }
    
    public ResponseList<User> getUserSuggestions(final String categorySlug) throws TwitterException {
        HttpResponse res;
        try {
            res = this.get(this.conf.getRestBaseURL() + "users/suggestions/" + URLEncoder.encode(categorySlug, "UTF-8") + ".json");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return this.factory.createUserListFromJSONArray_Users(res);
    }
    
    public ResponseList<Category> getSuggestedUserCategories() throws TwitterException {
        return this.factory.createCategoryList(this.get(this.conf.getRestBaseURL() + "users/suggestions.json"));
    }
    
    public ResponseList<User> getMemberSuggestions(final String categorySlug) throws TwitterException {
        HttpResponse res;
        try {
            res = this.get(this.conf.getRestBaseURL() + "users/suggestions/" + URLEncoder.encode(categorySlug, "UTF-8") + "/members.json");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return this.factory.createUserListFromJSONArray(res);
    }
    
    public ResponseList<Status> getFavorites() throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "favorites/list.json"));
    }
    
    public ResponseList<Status> getFavorites(final long userId) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "favorites/list.json?user_id=" + userId));
    }
    
    public ResponseList<Status> getFavorites(final String screenName) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "favorites/list.json", new HttpParameter("screen_name", screenName)));
    }
    
    public ResponseList<Status> getFavorites(final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "favorites/list.json", paging.asPostParameterArray()));
    }
    
    public ResponseList<Status> getFavorites(final long userId, final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "favorites/list.json", this.mergeParameters(new HttpParameter[] { new HttpParameter("user_id", userId) }, paging.asPostParameterArray())));
    }
    
    public ResponseList<Status> getFavorites(final String screenName, final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "favorites/list.json", this.mergeParameters(new HttpParameter[] { new HttpParameter("screen_name", screenName) }, paging.asPostParameterArray())));
    }
    
    public Status destroyFavorite(final long id) throws TwitterException {
        return this.factory.createStatus(this.post(this.conf.getRestBaseURL() + "favorites/destroy.json?id=" + id));
    }
    
    public Status createFavorite(final long id) throws TwitterException {
        return this.factory.createStatus(this.post(this.conf.getRestBaseURL() + "favorites/create.json?id=" + id));
    }
    
    public ResponseList<UserList> getUserLists(final String listOwnerScreenName) throws TwitterException {
        return this.getUserLists(listOwnerScreenName, false);
    }
    
    public ResponseList<UserList> getUserLists(final String listOwnerScreenName, final boolean reverse) throws TwitterException {
        return this.factory.createUserListList(this.get(this.conf.getRestBaseURL() + "lists/list.json", new HttpParameter("screen_name", listOwnerScreenName), new HttpParameter("reverse", reverse)));
    }
    
    public ResponseList<UserList> getUserLists(final long listOwnerUserId) throws TwitterException {
        return this.getUserLists(listOwnerUserId, false);
    }
    
    public ResponseList<UserList> getUserLists(final long listOwnerUserId, final boolean reverse) throws TwitterException {
        return this.factory.createUserListList(this.get(this.conf.getRestBaseURL() + "lists/list.json", new HttpParameter("user_id", listOwnerUserId), new HttpParameter("reverse", reverse)));
    }
    
    public ResponseList<Status> getUserListStatuses(final long listId, final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "lists/statuses.json", this.mergeParameters(paging.asPostParameterArray(Paging.SMCP, "count"), new HttpParameter("list_id", listId))));
    }
    
    public ResponseList<Status> getUserListStatuses(final long ownerId, final String slug, final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "lists/statuses.json", this.mergeParameters(paging.asPostParameterArray(Paging.SMCP, "count"), new HttpParameter[] { new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug) })));
    }
    
    public ResponseList<Status> getUserListStatuses(final String ownerScreenName, final String slug, final Paging paging) throws TwitterException {
        return this.factory.createStatusList(this.get(this.conf.getRestBaseURL() + "lists/statuses.json", this.mergeParameters(paging.asPostParameterArray(Paging.SMCP, "count"), new HttpParameter[] { new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug) })));
    }
    
    public UserList destroyUserListMember(final long listId, final long userId) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/destroy.json", new HttpParameter("list_id", listId), new HttpParameter("user_id", userId)));
    }
    
    public UserList destroyUserListMember(final long ownerId, final String slug, final long userId) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/destroy.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("user_id", userId)));
    }
    
    public UserList destroyUserListMember(final long listId, final String screenName) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/destroy.json", new HttpParameter("list_id", listId), new HttpParameter("screen_name", screenName)));
    }
    
    public UserList destroyUserListMember(final String ownerScreenName, final String slug, final long userId) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/destroy.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("user_id", userId)));
    }
    
    public UserList destroyUserListMembers(final long listId, final String[] screenNames) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/destroy_all.json", new HttpParameter("list_id", listId), new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }
    
    public UserList destroyUserListMembers(final long listId, final long[] userIds) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/destroy_all.json", new HttpParameter("list_id", listId), new HttpParameter("user_id", StringUtil.join(userIds))));
    }
    
    public UserList destroyUserListMembers(final String ownerScreenName, final String slug, final String[] screenNames) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/destroy_all.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }
    
    public PagableResponseList<UserList> getUserListMemberships(final long cursor) throws TwitterException {
        return this.getUserListMemberships(20, cursor);
    }
    
    public PagableResponseList<UserList> getUserListMemberships(final int count, final long cursor) throws TwitterException {
        return this.factory.createPagableUserListList(this.get(this.conf.getRestBaseURL() + "lists/memberships.json", new HttpParameter("cursor", cursor), new HttpParameter("count", count)));
    }
    
    public PagableResponseList<UserList> getUserListMemberships(final String listMemberScreenName, final long cursor) throws TwitterException {
        return this.getUserListMemberships(listMemberScreenName, cursor, false);
    }
    
    public PagableResponseList<UserList> getUserListMemberships(final String listMemberScreenName, final int count, final long cursor) throws TwitterException {
        return this.getUserListMemberships(listMemberScreenName, count, cursor, false);
    }
    
    public PagableResponseList<UserList> getUserListMemberships(final String listMemberScreenName, final long cursor, final boolean filterToOwnedLists) throws TwitterException {
        return this.getUserListMemberships(listMemberScreenName, 20, cursor, filterToOwnedLists);
    }
    
    public PagableResponseList<UserList> getUserListMemberships(final String listMemberScreenName, final int count, final long cursor, final boolean filterToOwnedLists) throws TwitterException {
        return this.factory.createPagableUserListList(this.get(this.conf.getRestBaseURL() + "lists/memberships.json", new HttpParameter("screen_name", listMemberScreenName), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("filter_to_owned_lists", filterToOwnedLists)));
    }
    
    public PagableResponseList<UserList> getUserListMemberships(final long listMemberId, final long cursor) throws TwitterException {
        return this.getUserListMemberships(listMemberId, cursor, false);
    }
    
    public PagableResponseList<UserList> getUserListMemberships(final long listMemberId, final int count, final long cursor) throws TwitterException {
        return this.getUserListMemberships(listMemberId, count, cursor, false);
    }
    
    public PagableResponseList<UserList> getUserListMemberships(final long listMemberId, final long cursor, final boolean filterToOwnedLists) throws TwitterException {
        return this.getUserListMemberships(listMemberId, 20, cursor, filterToOwnedLists);
    }
    
    public PagableResponseList<UserList> getUserListMemberships(final long listMemberId, final int count, final long cursor, final boolean filterToOwnedLists) throws TwitterException {
        return this.factory.createPagableUserListList(this.get(this.conf.getRestBaseURL() + "lists/memberships.json", new HttpParameter("user_id", listMemberId), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("filter_to_owned_lists", filterToOwnedLists)));
    }
    
    public PagableResponseList<User> getUserListSubscribers(final long listId, final long cursor) throws TwitterException {
        return this.getUserListSubscribers(listId, 20, cursor, false);
    }
    
    public PagableResponseList<User> getUserListSubscribers(final long listId, final int count, final long cursor) throws TwitterException {
        return this.getUserListSubscribers(listId, count, cursor, false);
    }
    
    public PagableResponseList<User> getUserListSubscribers(final long listId, final int count, final long cursor, final boolean skipStatus) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "lists/subscribers.json", new HttpParameter("list_id", listId), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }
    
    public PagableResponseList<User> getUserListSubscribers(final long ownerId, final String slug, final long cursor) throws TwitterException {
        return this.getUserListSubscribers(ownerId, slug, 20, cursor, false);
    }
    
    public PagableResponseList<User> getUserListSubscribers(final long ownerId, final String slug, final int count, final long cursor) throws TwitterException {
        return this.getUserListSubscribers(ownerId, slug, count, cursor, false);
    }
    
    public PagableResponseList<User> getUserListSubscribers(final long ownerId, final String slug, final int count, final long cursor, final boolean skipStatus) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "lists/subscribers.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }
    
    public PagableResponseList<User> getUserListSubscribers(final String ownerScreenName, final String slug, final long cursor) throws TwitterException {
        return this.getUserListSubscribers(ownerScreenName, slug, 20, cursor, false);
    }
    
    public PagableResponseList<User> getUserListSubscribers(final String ownerScreenName, final String slug, final int count, final long cursor) throws TwitterException {
        return this.getUserListSubscribers(ownerScreenName, slug, count, cursor, false);
    }
    
    public PagableResponseList<User> getUserListSubscribers(final String ownerScreenName, final String slug, final int count, final long cursor, final boolean skipStatus) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "lists/subscribers.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }
    
    public UserList createUserListSubscription(final long listId) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/subscribers/create.json", new HttpParameter("list_id", listId)));
    }
    
    public UserList createUserListSubscription(final long ownerId, final String slug) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/subscribers/create.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)));
    }
    
    public UserList createUserListSubscription(final String ownerScreenName, final String slug) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/subscribers/create.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)));
    }
    
    public User showUserListSubscription(final long listId, final long userId) throws TwitterException {
        return this.factory.createUser(this.get(this.conf.getRestBaseURL() + "lists/subscribers/show.json?list_id=" + listId + "&user_id=" + userId));
    }
    
    public User showUserListSubscription(final long ownerId, final String slug, final long userId) throws TwitterException {
        return this.factory.createUser(this.get(this.conf.getRestBaseURL() + "lists/subscribers/show.json?owner_id=" + ownerId + "&slug=" + slug + "&user_id=" + userId));
    }
    
    public User showUserListSubscription(final String ownerScreenName, final String slug, final long userId) throws TwitterException {
        return this.factory.createUser(this.get(this.conf.getRestBaseURL() + "lists/subscribers/show.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("user_id", userId)));
    }
    
    public UserList destroyUserListSubscription(final long listId) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/subscribers/destroy.json", new HttpParameter("list_id", listId)));
    }
    
    public UserList destroyUserListSubscription(final long ownerId, final String slug) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/subscribers/destroy.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)));
    }
    
    public UserList destroyUserListSubscription(final String ownerScreenName, final String slug) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/subscribers/destroy.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)));
    }
    
    public UserList createUserListMembers(final long listId, final long... userIds) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter("list_id", listId), new HttpParameter("user_id", StringUtil.join(userIds))));
    }
    
    public UserList createUserListMembers(final long ownerId, final String slug, final long... userIds) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("user_id", StringUtil.join(userIds))));
    }
    
    public UserList createUserListMembers(final String ownerScreenName, final String slug, final long... userIds) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("user_id", StringUtil.join(userIds))));
    }
    
    public UserList createUserListMembers(final long listId, final String... screenNames) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter("list_id", listId), new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }
    
    public UserList createUserListMembers(final long ownerId, final String slug, final String... screenNames) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }
    
    public UserList createUserListMembers(final String ownerScreenName, final String slug, final String... screenNames) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }
    
    public User showUserListMembership(final long listId, final long userId) throws TwitterException {
        return this.factory.createUser(this.get(this.conf.getRestBaseURL() + "lists/members/show.json?list_id=" + listId + "&user_id=" + userId));
    }
    
    public User showUserListMembership(final long ownerId, final String slug, final long userId) throws TwitterException {
        return this.factory.createUser(this.get(this.conf.getRestBaseURL() + "lists/members/show.json?owner_id=" + ownerId + "&slug=" + slug + "&user_id=" + userId));
    }
    
    public User showUserListMembership(final String ownerScreenName, final String slug, final long userId) throws TwitterException {
        return this.factory.createUser(this.get(this.conf.getRestBaseURL() + "lists/members/show.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("user_id", userId)));
    }
    
    public PagableResponseList<User> getUserListMembers(final long listId, final long cursor) throws TwitterException {
        return this.getUserListMembers(listId, 20, cursor, false);
    }
    
    public PagableResponseList<User> getUserListMembers(final long listId, final int count, final long cursor) throws TwitterException {
        return this.getUserListMembers(listId, count, cursor, false);
    }
    
    public PagableResponseList<User> getUserListMembers(final long listId, final int count, final long cursor, final boolean skipStatus) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "lists/members.json", new HttpParameter("list_id", listId), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }
    
    public PagableResponseList<User> getUserListMembers(final long ownerId, final String slug, final long cursor) throws TwitterException {
        return this.getUserListMembers(ownerId, slug, 20, cursor, false);
    }
    
    public PagableResponseList<User> getUserListMembers(final long ownerId, final String slug, final int count, final long cursor) throws TwitterException {
        return this.getUserListMembers(ownerId, slug, count, cursor, false);
    }
    
    public PagableResponseList<User> getUserListMembers(final long ownerId, final String slug, final int count, final long cursor, final boolean skipStatus) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "lists/members.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }
    
    public PagableResponseList<User> getUserListMembers(final String ownerScreenName, final String slug, final long cursor) throws TwitterException {
        return this.getUserListMembers(ownerScreenName, slug, 20, cursor, false);
    }
    
    public PagableResponseList<User> getUserListMembers(final String ownerScreenName, final String slug, final int count, final long cursor) throws TwitterException {
        return this.getUserListMembers(ownerScreenName, slug, count, cursor, false);
    }
    
    public PagableResponseList<User> getUserListMembers(final String ownerScreenName, final String slug, final int count, final long cursor, final boolean skipStatus) throws TwitterException {
        return this.factory.createPagableUserList(this.get(this.conf.getRestBaseURL() + "lists/members.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }
    
    public UserList createUserListMember(final long listId, final long userId) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/create.json", new HttpParameter("user_id", userId), new HttpParameter("list_id", listId)));
    }
    
    public UserList createUserListMember(final long ownerId, final String slug, final long userId) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/create.json", new HttpParameter("user_id", userId), new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)));
    }
    
    public UserList createUserListMember(final String ownerScreenName, final String slug, final long userId) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/members/create.json", new HttpParameter("user_id", userId), new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)));
    }
    
    public UserList destroyUserList(final long listId) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/destroy.json", new HttpParameter("list_id", listId)));
    }
    
    public UserList destroyUserList(final long ownerId, final String slug) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/destroy.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)));
    }
    
    public UserList destroyUserList(final String ownerScreenName, final String slug) throws TwitterException {
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/destroy.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)));
    }
    
    public UserList updateUserList(final long listId, final String newListName, final boolean isPublicList, final String newDescription) throws TwitterException {
        return this.updateUserList(newListName, isPublicList, newDescription, new HttpParameter("list_id", listId));
    }
    
    public UserList updateUserList(final long ownerId, final String slug, final String newListName, final boolean isPublicList, final String newDescription) throws TwitterException {
        return this.updateUserList(newListName, isPublicList, newDescription, new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug));
    }
    
    public UserList updateUserList(final String ownerScreenName, final String slug, final String newListName, final boolean isPublicList, final String newDescription) throws TwitterException {
        return this.updateUserList(newListName, isPublicList, newDescription, new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug));
    }
    
    private UserList updateUserList(final String newListName, final boolean isPublicList, final String newDescription, final HttpParameter... params) throws TwitterException {
        final List<HttpParameter> httpParams = new ArrayList<HttpParameter>();
        Collections.addAll(httpParams, params);
        if (newListName != null) {
            httpParams.add(new HttpParameter("name", newListName));
        }
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (newDescription != null) {
            httpParams.add(new HttpParameter("description", newDescription));
        }
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/update.json", (HttpParameter[])httpParams.toArray(new HttpParameter[httpParams.size()])));
    }
    
    public UserList createUserList(final String listName, final boolean isPublicList, final String description) throws TwitterException {
        final List<HttpParameter> httpParams = new ArrayList<HttpParameter>();
        httpParams.add(new HttpParameter("name", listName));
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (description != null) {
            httpParams.add(new HttpParameter("description", description));
        }
        return this.factory.createAUserList(this.post(this.conf.getRestBaseURL() + "lists/create.json", (HttpParameter[])httpParams.toArray(new HttpParameter[httpParams.size()])));
    }
    
    public UserList showUserList(final long listId) throws TwitterException {
        return this.factory.createAUserList(this.get(this.conf.getRestBaseURL() + "lists/show.json?list_id=" + listId));
    }
    
    public UserList showUserList(final long ownerId, final String slug) throws TwitterException {
        return this.factory.createAUserList(this.get(this.conf.getRestBaseURL() + "lists/show.json?owner_id=" + ownerId + "&slug=" + slug));
    }
    
    public UserList showUserList(final String ownerScreenName, final String slug) throws TwitterException {
        return this.factory.createAUserList(this.get(this.conf.getRestBaseURL() + "lists/show.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)));
    }
    
    public PagableResponseList<UserList> getUserListSubscriptions(final String listSubscriberScreenName, final long cursor) throws TwitterException {
        return this.getUserListSubscriptions(listSubscriberScreenName, 20, cursor);
    }
    
    public PagableResponseList<UserList> getUserListSubscriptions(final String listSubscriberScreenName, final int count, final long cursor) throws TwitterException {
        return this.factory.createPagableUserListList(this.get(this.conf.getRestBaseURL() + "lists/subscriptions.json", new HttpParameter("screen_name", listSubscriberScreenName), new HttpParameter("count", count), new HttpParameter("cursor", cursor)));
    }
    
    public PagableResponseList<UserList> getUserListSubscriptions(final long listSubscriberId, final long cursor) throws TwitterException {
        return this.getUserListSubscriptions(listSubscriberId, 20, cursor);
    }
    
    public PagableResponseList<UserList> getUserListSubscriptions(final long listSubscriberId, final int count, final long cursor) throws TwitterException {
        return this.factory.createPagableUserListList(this.get(this.conf.getRestBaseURL() + "lists/subscriptions.json", new HttpParameter("user_id", listSubscriberId), new HttpParameter("count", count), new HttpParameter("cursor", cursor)));
    }
    
    public PagableResponseList<UserList> getUserListsOwnerships(final String listOwnerScreenName, final long cursor) throws TwitterException {
        return this.getUserListsOwnerships(listOwnerScreenName, 20, cursor);
    }
    
    public PagableResponseList<UserList> getUserListsOwnerships(final String listOwnerScreenName, final int count, final long cursor) throws TwitterException {
        return this.factory.createPagableUserListList(this.get(this.conf.getRestBaseURL() + "lists/ownerships.json", new HttpParameter("screen_name", listOwnerScreenName), new HttpParameter("count", count), new HttpParameter("cursor", cursor)));
    }
    
    public PagableResponseList<UserList> getUserListsOwnerships(final long listOwnerId, final long cursor) throws TwitterException {
        return this.getUserListsOwnerships(listOwnerId, 20, cursor);
    }
    
    public PagableResponseList<UserList> getUserListsOwnerships(final long listOwnerId, final int count, final long cursor) throws TwitterException {
        return this.factory.createPagableUserListList(this.get(this.conf.getRestBaseURL() + "lists/ownerships.json", new HttpParameter("user_id", listOwnerId), new HttpParameter("count", count), new HttpParameter("cursor", cursor)));
    }
    
    public ResponseList<SavedSearch> getSavedSearches() throws TwitterException {
        return this.factory.createSavedSearchList(this.get(this.conf.getRestBaseURL() + "saved_searches/list.json"));
    }
    
    public SavedSearch showSavedSearch(final long id) throws TwitterException {
        return this.factory.createSavedSearch(this.get(this.conf.getRestBaseURL() + "saved_searches/show/" + id + ".json"));
    }
    
    public SavedSearch createSavedSearch(final String query) throws TwitterException {
        return this.factory.createSavedSearch(this.post(this.conf.getRestBaseURL() + "saved_searches/create.json", new HttpParameter("query", query)));
    }
    
    public SavedSearch destroySavedSearch(final long id) throws TwitterException {
        return this.factory.createSavedSearch(this.post(this.conf.getRestBaseURL() + "saved_searches/destroy/" + id + ".json"));
    }
    
    public Place getGeoDetails(final String placeId) throws TwitterException {
        return this.factory.createPlace(this.get(this.conf.getRestBaseURL() + "geo/id/" + placeId + ".json"));
    }
    
    public ResponseList<Place> reverseGeoCode(final GeoQuery query) throws TwitterException {
        try {
            return this.factory.createPlaceList(this.get(this.conf.getRestBaseURL() + "geo/reverse_geocode.json", query.asHttpParameterArray()));
        }
        catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                return this.factory.createEmptyResponseList();
            }
            throw te;
        }
    }
    
    public ResponseList<Place> searchPlaces(final GeoQuery query) throws TwitterException {
        return this.factory.createPlaceList(this.get(this.conf.getRestBaseURL() + "geo/search.json", query.asHttpParameterArray()));
    }
    
    public ResponseList<Place> getSimilarPlaces(final GeoLocation location, final String name, final String containedWithin, final String streetAddress) throws TwitterException {
        final List<HttpParameter> params = new ArrayList<HttpParameter>(3);
        params.add(new HttpParameter("lat", location.getLatitude()));
        params.add(new HttpParameter("long", location.getLongitude()));
        params.add(new HttpParameter("name", name));
        if (containedWithin != null) {
            params.add(new HttpParameter("contained_within", containedWithin));
        }
        if (streetAddress != null) {
            params.add(new HttpParameter("attribute:street_address", streetAddress));
        }
        return this.factory.createPlaceList(this.get(this.conf.getRestBaseURL() + "geo/similar_places.json", (HttpParameter[])params.toArray(new HttpParameter[params.size()])));
    }
    
    public Trends getPlaceTrends(final int woeid) throws TwitterException {
        return this.factory.createTrends(this.get(this.conf.getRestBaseURL() + "trends/place.json?id=" + woeid));
    }
    
    public ResponseList<Location> getAvailableTrends() throws TwitterException {
        return this.factory.createLocationList(this.get(this.conf.getRestBaseURL() + "trends/available.json"));
    }
    
    public ResponseList<Location> getClosestTrends(final GeoLocation location) throws TwitterException {
        return this.factory.createLocationList(this.get(this.conf.getRestBaseURL() + "trends/closest.json", new HttpParameter("lat", location.getLatitude()), new HttpParameter("long", location.getLongitude())));
    }
    
    public User reportSpam(final long userId) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "users/report_spam.json?user_id=" + userId));
    }
    
    public User reportSpam(final String screenName) throws TwitterException {
        return this.factory.createUser(this.post(this.conf.getRestBaseURL() + "users/report_spam.json", new HttpParameter("screen_name", screenName)));
    }
    
    public TwitterAPIConfiguration getAPIConfiguration() throws TwitterException {
        return this.factory.createTwitterAPIConfiguration(this.get(this.conf.getRestBaseURL() + "help/configuration.json"));
    }
    
    public ResponseList<HelpResources.Language> getLanguages() throws TwitterException {
        return this.factory.createLanguageList(this.get(this.conf.getRestBaseURL() + "help/languages.json"));
    }
    
    public String getPrivacyPolicy() throws TwitterException {
        try {
            return this.get(this.conf.getRestBaseURL() + "help/privacy.json").asJSONObject().getString("privacy");
        }
        catch (JSONException e) {
            throw new TwitterException(e);
        }
    }
    
    public String getTermsOfService() throws TwitterException {
        try {
            return this.get(this.conf.getRestBaseURL() + "help/tos.json").asJSONObject().getString("tos");
        }
        catch (JSONException e) {
            throw new TwitterException(e);
        }
    }
    
    public Map<String, RateLimitStatus> getRateLimitStatus() throws TwitterException {
        return this.factory.createRateLimitStatuses(this.get(this.conf.getRestBaseURL() + "application/rate_limit_status.json"));
    }
    
    public Map<String, RateLimitStatus> getRateLimitStatus(final String... resources) throws TwitterException {
        return this.factory.createRateLimitStatuses(this.get(this.conf.getRestBaseURL() + "application/rate_limit_status.json?resources=" + StringUtil.join(resources)));
    }
    
    public TimelinesResources timelines() {
        return this;
    }
    
    public TweetsResources tweets() {
        return this;
    }
    
    public SearchResource search() {
        return this;
    }
    
    public DirectMessagesResources directMessages() {
        return this;
    }
    
    public FriendsFollowersResources friendsFollowers() {
        return this;
    }
    
    public UsersResources users() {
        return this;
    }
    
    public SuggestedUsersResources suggestedUsers() {
        return this;
    }
    
    public FavoritesResources favorites() {
        return this;
    }
    
    public ListsResources list() {
        return this;
    }
    
    public SavedSearchesResources savedSearches() {
        return this;
    }
    
    public PlacesGeoResources placesGeo() {
        return this;
    }
    
    public TrendsResources trends() {
        return this;
    }
    
    public SpamReportingResource spamReporting() {
        return this;
    }
    
    public HelpResources help() {
        return this;
    }
    
    private HttpResponse get(String url) throws TwitterException {
        this.ensureAuthorizationEnabled();
        if (this.IMPLICIT_PARAMS_STR.length() > 0) {
            if (url.contains("?")) {
                url = url + "&" + this.IMPLICIT_PARAMS_STR;
            }
            else {
                url = url + "?" + this.IMPLICIT_PARAMS_STR;
            }
        }
        if (!this.conf.isMBeanEnabled()) {
            return this.http.get(url, null, this.auth, this);
        }
        HttpResponse response = null;
        final long start = System.currentTimeMillis();
        try {
            response = this.http.get(url, null, this.auth, this);
        }
        finally {
            final long elapsedTime = System.currentTimeMillis() - start;
            TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, this.isOk(response));
        }
        return response;
    }
    
    private HttpResponse get(final String url, final HttpParameter... params) throws TwitterException {
        this.ensureAuthorizationEnabled();
        if (!this.conf.isMBeanEnabled()) {
            return this.http.get(url, this.mergeImplicitParams(params), this.auth, this);
        }
        HttpResponse response = null;
        final long start = System.currentTimeMillis();
        try {
            response = this.http.get(url, this.mergeImplicitParams(params), this.auth, this);
        }
        finally {
            final long elapsedTime = System.currentTimeMillis() - start;
            TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, this.isOk(response));
        }
        return response;
    }
    
    private HttpResponse post(final String url) throws TwitterException {
        this.ensureAuthorizationEnabled();
        if (!this.conf.isMBeanEnabled()) {
            return this.http.post(url, this.IMPLICIT_PARAMS, this.auth, this);
        }
        HttpResponse response = null;
        final long start = System.currentTimeMillis();
        try {
            response = this.http.post(url, this.IMPLICIT_PARAMS, this.auth, this);
        }
        finally {
            final long elapsedTime = System.currentTimeMillis() - start;
            TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, this.isOk(response));
        }
        return response;
    }
    
    private HttpResponse post(final String url, final HttpParameter... params) throws TwitterException {
        this.ensureAuthorizationEnabled();
        if (!this.conf.isMBeanEnabled()) {
            return this.http.post(url, this.mergeImplicitParams(params), this.auth, this);
        }
        HttpResponse response = null;
        final long start = System.currentTimeMillis();
        try {
            response = this.http.post(url, this.mergeImplicitParams(params), this.auth, this);
        }
        finally {
            final long elapsedTime = System.currentTimeMillis() - start;
            TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, this.isOk(response));
        }
        return response;
    }
    
    private HttpParameter[] mergeParameters(final HttpParameter[] params1, final HttpParameter[] params2) {
        if (params1 != null && params2 != null) {
            final HttpParameter[] params3 = new HttpParameter[params1.length + params2.length];
            System.arraycopy(params1, 0, params3, 0, params1.length);
            System.arraycopy(params2, 0, params3, params1.length, params2.length);
            return params3;
        }
        if (null == params1 && null == params2) {
            return new HttpParameter[0];
        }
        if (params1 != null) {
            return params1;
        }
        return params2;
    }
    
    private HttpParameter[] mergeParameters(final HttpParameter[] params1, final HttpParameter params2) {
        if (params1 != null && params2 != null) {
            final HttpParameter[] params3 = new HttpParameter[params1.length + 1];
            System.arraycopy(params1, 0, params3, 0, params1.length);
            params3[params3.length - 1] = params2;
            return params3;
        }
        if (null == params1 && null == params2) {
            return new HttpParameter[0];
        }
        if (params1 != null) {
            return params1;
        }
        return new HttpParameter[] { params2 };
    }
    
    private HttpParameter[] mergeImplicitParams(final HttpParameter... params) {
        return this.mergeParameters(params, this.IMPLICIT_PARAMS);
    }
    
    private boolean isOk(final HttpResponse response) {
        return response != null && response.getStatusCode() < 300;
    }
    
    public String toString() {
        return "TwitterImpl{INCLUDE_MY_RETWEET=" + this.INCLUDE_MY_RETWEET + '}';
    }
    
    static {
        implicitParamsMap = new ConcurrentHashMap<Configuration, HttpParameter[]>();
        implicitParamsStrMap = new ConcurrentHashMap<Configuration, String>();
    }
}
